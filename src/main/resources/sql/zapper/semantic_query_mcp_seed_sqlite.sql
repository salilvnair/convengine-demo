-- Zapper semantic query MCP seed (SQLite)
-- Purpose: enable end-to-end natural-language -> db.semantic.query flow.

-- -----------------------------------------------------------------------------
-- Cleanup (idempotent)
-- -----------------------------------------------------------------------------
DELETE FROM ce_rule WHERE intent_code = 'SEMANTIC_QUERY';
DELETE FROM ce_response WHERE intent_code = 'SEMANTIC_QUERY';
DELETE FROM ce_prompt_template WHERE intent_code = 'SEMANTIC_QUERY';
DELETE FROM ce_output_schema WHERE intent_code = 'SEMANTIC_QUERY';
DELETE FROM ce_intent_classifier WHERE intent_code = 'SEMANTIC_QUERY';
DELETE FROM ce_mcp_planner WHERE planner_id = 5301;
DELETE FROM ce_mcp_tool WHERE tool_code = 'db.semantic.query';
DELETE FROM ce_verbose WHERE intent_code = 'SEMANTIC_QUERY';
DELETE FROM ce_intent WHERE intent_code = 'SEMANTIC_QUERY';

-- -----------------------------------------------------------------------------
-- Intent + classifier
-- -----------------------------------------------------------------------------
INSERT OR REPLACE INTO ce_intent (intent_code, description, priority, enabled, display_name, llm_hint)
VALUES
('SEMANTIC_QUERY', 'Run semantic query planning and SQL execution against Zapper schema', 45, 1, 'Semantic Query',
 'Use semantic model + graph join path + AST generation to answer DB questions.');

INSERT OR REPLACE INTO ce_intent_classifier (intent_code, state_code, rule_type, pattern, priority, enabled, description)
VALUES
('SEMANTIC_QUERY', 'UNKNOWN', 'REGEX',
 '(?i)\\b(disconnect|termination|downstream|request status|failed request|billbank|zapper|account status|join|sql|query)\\b',
 40, 1,
 'Classifier for semantic query over zapper domain');

-- -----------------------------------------------------------------------------
-- Optional extraction schema
-- -----------------------------------------------------------------------------
INSERT OR REPLACE INTO ce_output_schema (intent_code, state_code, json_schema, description, enabled, priority)
VALUES
(
  'SEMANTIC_QUERY',
  'ANALYZE',
  '{
    "type":"object",
    "properties":{
      "accountId":{"type":"string"},
      "requestId":{"type":"string"},
      "timeWindow":{"type":"string"}
    }
  }',
  'Optional fields for semantic query prompts',
  1,
  1
);

-- -----------------------------------------------------------------------------
-- Prompt template
-- -----------------------------------------------------------------------------
INSERT OR REPLACE INTO ce_prompt_template
(intent_code, state_code, response_type, system_prompt, user_prompt, temperature, interaction_mode, interaction_contract, enabled)
VALUES
(
  'SEMANTIC_QUERY',
  'ANALYZE',
  'DERIVED',
  'You are a semantic DB diagnostics assistant. Use MCP outputs only and avoid hallucinations.',
  'User input: {{user_input}}\nContext: {{context}}\nMCP: {{mcp_observations}}',
  0.00,
  'MCP',
  NULL,
  1
);

-- -----------------------------------------------------------------------------
-- Response config
-- -----------------------------------------------------------------------------
INSERT OR REPLACE INTO ce_response
(intent_code, state_code, output_format, response_type, exact_text, derivation_hint, json_schema, priority, enabled, description)
VALUES
(
  'SEMANTIC_QUERY',
  'COMPLETED',
  'TEXT',
  'DERIVED',
  NULL,
  'Render concise final answer from MCP final answer for semantic query.',
  NULL,
  10,
  1,
  'Semantic query completed response derived from MCP final answer'
),
(
  'SEMANTIC_QUERY',
  'FAILED',
  'TEXT',
  'DERIVED',
  NULL,
  'If context.mcp.lifecycle.errorMessage exists, explain the failure briefly and ask for refined query filters. Otherwise provide a safe retry message.',
  NULL,
  9,
  1,
  'Semantic query failure response derived from MCP lifecycle error details'
);

-- -----------------------------------------------------------------------------
-- Rules
-- -----------------------------------------------------------------------------
INSERT OR REPLACE INTO ce_rule
(phase, intent_code, state_code, rule_type, match_pattern, action, action_value, priority, enabled, description)
VALUES
('POST_AGENT_INTENT', 'SEMANTIC_QUERY', 'UNKNOWN', 'REGEX', '.*', 'SET_STATE', 'ANALYZE', 70, 1,
 'Bootstrap SEMANTIC_QUERY into ANALYZE when classifier sets UNKNOWN'),
('POST_AGENT_INTENT', 'SEMANTIC_QUERY', 'IDLE', 'REGEX', '.*', 'SET_STATE', 'ANALYZE', 71, 1,
 'Bootstrap SEMANTIC_QUERY into ANALYZE from IDLE'),
('POST_AGENT_MCP', 'SEMANTIC_QUERY', 'ANALYZE', 'JSON_PATH',
 '$[?(@.context.mcp.lifecycle.error==true || @.context.mcp.lifecycle.blocked==true || @.context.mcp.lifecycle.status == ''TOOL_ERROR'' || @.context.mcp.lifecycle.status == ''FALLBACK'' || @.context.mcp.lifecycle.outcome == ''ERROR'')]',
 'SET_STATE', 'FAILED', 72, 1,
 'Move SEMANTIC_QUERY to FAILED when MCP lifecycle indicates error/blocked/fallback'),
('POST_AGENT_MCP', 'SEMANTIC_QUERY', 'ANALYZE', 'JSON_PATH',
 '$[?(@.context.mcp.finalAnswer != ''null'' && @.context.mcp.finalAnswer != null && @.context.mcp.finalAnswer != '''')]',
 'SET_STATE', 'COMPLETED', 73, 1,
 'Move SEMANTIC_QUERY to COMPLETED when context.mcp.finalAnswer exists'),
('PRE_RESPONSE_RESOLUTION', 'SEMANTIC_QUERY', 'ANY', 'REGEX', '(?i)\\b(reset|restart|start over)\\b', 'SET_STATE', 'IDLE', 74, 1,
 'Allow reset for semantic query flow');

-- -----------------------------------------------------------------------------
-- ce_verbose stage diagnostics for semantic pipeline
-- -----------------------------------------------------------------------------
INSERT INTO ce_verbose
(intent_code, state_code, step_match, step_value, determinant, rule_id, tool_code, message, error_message, priority, enabled)
VALUES
('SEMANTIC_QUERY', 'ANALYZE', 'REGEX', '^Semantic.*Stage$', 'SEMANTIC_STAGE_ENTER', NULL, 'db.semantic.query',
 'Semantic stage [[${stageCode}]] started. (entityCount=[[${retrievalEntitiesCount}]], tableCount=[[${retrievalTablesCount}]])',
 'Failed to start semantic stage [[${stageCode}]].',
 12, 1),
('SEMANTIC_QUERY', 'ANALYZE', 'REGEX', '^Semantic.*Stage$', 'SEMANTIC_STAGE_EXIT', NULL, 'db.semantic.query',
 'Semantic stage [[${stageCode}]] completed. (astValid=[[${astValid}]], rows=[[${executionRowCount}]])',
 'Semantic stage [[${stageCode}]] completed with issues.',
 13, 1),
('SEMANTIC_QUERY', 'ANALYZE', 'REGEX', '^Semantic.*Stage$', 'SEMANTIC_STAGE_ERROR', NULL, 'db.semantic.query',
 'Semantic stage [[${stageCode}]] failed.',
 'Semantic stage [[${stageCode}]] failed: [[${errorMessage}]]',
 5, 1),
('SEMANTIC_QUERY', 'ANALYZE', 'EXACT', 'SemanticQueryRuntimeService', 'SEMANTIC_RUNTIME_ERROR', NULL, 'db.semantic.query',
 'Semantic runtime failed.',
 'Semantic runtime failed: [[${errorMessage}]]',
 4, 1),
('SEMANTIC_QUERY', 'ANALYZE', 'EXACT', 'DefaultSemanticAstGenerator', 'SEMANTIC_AST_LLM_INPUT', NULL, 'db.semantic.query',
 'Generating semantic AST using LLM.',
 'Failed before AST LLM generation.',
 11, 1),
('SEMANTIC_QUERY', 'ANALYZE', 'EXACT', 'DefaultSemanticAstGenerator', 'SEMANTIC_AST_LLM_OUTPUT', NULL, 'db.semantic.query',
 'Semantic AST generated by LLM for entity [[${entity}]].',
 'AST LLM output could not be parsed.',
 11, 1),
('SEMANTIC_QUERY', 'ANALYZE', 'EXACT', 'DefaultSemanticAstGenerator', 'SEMANTIC_AST_LLM_ERROR', NULL, 'db.semantic.query',
 'Semantic AST generation failed.',
 'Semantic AST LLM error: [[${errorMessage}]]',
 4, 1),
('SEMANTIC_QUERY', 'ANALYZE', 'EXACT', 'SemanticJoinPathStage', 'SEMANTIC_SCHEMA_GRAPH_TRAVERSED', NULL, 'db.semantic.query',
 'Schema graph traversal completed with [[${edgesCount}]] edges.',
 'Schema graph traversal failed.',
 11, 1),
('SEMANTIC_QUERY', 'ANALYZE', 'EXACT', 'SemanticJoinPathStage', 'SEMANTIC_JOIN_PATH_RESOLVED', NULL, 'db.semantic.query',
 'Join path resolved from [[${baseTable}]] (unresolved=[[${unresolvedTablesCount}]]).',
 'Join path resolution failed.',
 11, 1),
('SEMANTIC_QUERY', 'ANALYZE', 'EXACT', 'SemanticAstGenerationStage', 'SEMANTIC_AST_GENERATED', NULL, 'db.semantic.query',
 'AST generated for entity [[${entity}]] (filters=[[${filterCount}]], limit=[[${limit}]])',
 'AST generation stage failed.',
 11, 1),
('SEMANTIC_QUERY', 'ANALYZE', 'EXACT', 'SemanticAstValidationStage', 'SEMANTIC_AST_VALIDATED', NULL, 'db.semantic.query',
 'AST validation completed. valid=[[${valid}]] errorCount=[[${errorCount}]].',
 'AST validation failed: [[${errors}]]',
 4, 1);

-- -----------------------------------------------------------------------------
-- MCP tool + planner
-- -----------------------------------------------------------------------------
INSERT OR REPLACE INTO ce_mcp_tool (tool_id, tool_code, tool_group, intent_code, state_code, enabled, description)
VALUES
(9301, 'db.semantic.query', 'DB', 'SEMANTIC_QUERY', 'ANALYZE', 1,
 'Execute semantic query pipeline (retrieval -> join path -> AST -> SQL -> execution)');

INSERT OR REPLACE INTO ce_mcp_planner (planner_id, intent_code, state_code, system_prompt, user_prompt, enabled, created_at)
VALUES
(
  5301,
  'SEMANTIC_QUERY',
  'ANALYZE',
  'You are an MCP planning agent for semantic DB querying. If question needs database facts, CALL_TOOL db.semantic.query. Use ANSWER only for pure greetings/chitchat. Return strict JSON only.',
  'User input:\n{{user_input}}\n\nCurrent date/time context:\n- current_date: {{current_date}}\n- current_datetime: {{current_datetime}}\n- current_year: {{current_year}}\n- current_timezone: {{current_timezone}}\n\nStandalone query:\n{{standalone_query}}\n\nRecent conversation history:\n{{conversation_history}}\n\nContext JSON:\n{{context}}\n\nAvailable MCP tools:\n{{mcp_tools}}\n\nExisting MCP observations:\n{{mcp_observations}}\n\nReturn strict JSON:\n{\n  "action":"CALL_TOOL" | "ANSWER",\n  "tool_code":"<tool_code_or_null>",\n  "args":{},\n  "answer":"<text_or_null>"\n}',
  1,
  CURRENT_TIMESTAMP
);

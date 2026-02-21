SET search_path TO v2, public;

-- Clean seedable tables (runtime tables intentionally skipped)
TRUNCATE TABLE ce_mcp_db_tool RESTART IDENTITY CASCADE;
TRUNCATE TABLE ce_mcp_tool RESTART IDENTITY CASCADE;
TRUNCATE TABLE ce_pending_action RESTART IDENTITY CASCADE;
TRUNCATE TABLE ce_rule RESTART IDENTITY CASCADE;
TRUNCATE TABLE ce_response RESTART IDENTITY CASCADE;
TRUNCATE TABLE ce_prompt_template RESTART IDENTITY CASCADE;
TRUNCATE TABLE ce_output_schema RESTART IDENTITY CASCADE;
TRUNCATE TABLE ce_intent_classifier RESTART IDENTITY CASCADE;
TRUNCATE TABLE ce_intent RESTART IDENTITY CASCADE;
TRUNCATE TABLE ce_container_config RESTART IDENTITY CASCADE;
TRUNCATE TABLE ce_policy RESTART IDENTITY CASCADE;
TRUNCATE TABLE ce_config RESTART IDENTITY CASCADE;
TRUNCATE TABLE zp_faq RESTART IDENTITY CASCADE;

-- -----------------------------------------------------------------------------
-- ce_config (core agent/planner prompts)
-- -----------------------------------------------------------------------------
INSERT INTO ce_config
(config_id, config_type, config_key, config_value, enabled, created_at)
VALUES(1, 'AgentIntentResolver', 'MIN_CONFIDENCE', '0.55', true, '2026-02-19 23:22:22.487');
INSERT INTO ce_config
(config_id, config_type, config_key, config_value, enabled, created_at)
VALUES(2, 'AgentIntentResolver', 'COLLISION_GAP_THRESHOLD', '0.20', true, '2026-02-19 23:22:22.487');
INSERT INTO ce_config
(config_id, config_type, config_key, config_value, enabled, created_at)
VALUES(3, 'IntentResolutionStep', 'STICKY_INTENT', 'true', true, '2026-02-19 23:22:22.487');
INSERT INTO ce_config
(config_id, config_type, config_key, config_value, enabled, created_at)
VALUES(4, 'McpPlanner', 'SYSTEM_PROMPT', 'You are an MCP planning agent. Decide CALL_TOOL or ANSWER. Be safe, conservative, and return JSON only.', true, '2026-02-19 23:22:22.487');
INSERT INTO ce_config
(config_id, config_type, config_key, config_value, enabled, created_at)
VALUES(5, 'McpPlanner', 'USER_PROMPT', 'User input:\n{{user_input}}\nContext:\n{{context}}\nTools:\n{{mcp_tools}}\nObservations:\n{{mcp_observations}}\nReturn strict planner JSON.', true, '2026-02-19 23:22:22.487');
INSERT INTO ce_config
(config_id, config_type, config_key, config_value, enabled, created_at)
VALUES(6, 'AgentIntentResolver', 'SYSTEM_PROMPT', 'You are an intent resolution agent for a conversational engine.
        You are a JSON generator. You must output valid JSON only. Do not include any explanations, greetings, or markdown formatting. Only return the JSON object.
                 Return JSON ONLY with fields:
                 {
                   "intent": "<INTENT_CODE_OR_NULL>",
                   "state": "INTENT_COLLISION | IDLE",
                   "confidence": 0.0,
                   "needsClarification": false,
                   "clarificationResolved": false,
                   "clarificationQuestion": "",
                   "intentScores": [{"intent":"<INTENT_CODE>","confidence":0.0}],
                   "followups": []
                 }
                CHAIN-OF-THOUGHT POLICY:
- Do NOT reveal chain-of-thought.
- Do NOT explain how you reached the answer.
- Summaries, reasoning, or internal thoughts are forbidden.
                 Rules:
CRITICAL OUTPUT RULES:
- DO NOT include reasoning, thoughts, or analysis.
- DO NOT use <think> tags or similar.
- Return ONLY valid JSON.
- intent MUST be the intent CODE, not an id or priority.
- confidence MUST be between 0.0 and 1.0.
- clarificationQuestion MUST be null when needsClarification=false.
                 - Score all plausible intents and return them in intentScores sorted by confidence descending.
                 - If top intents are close and ambiguous, set state to INTENT_COLLISION and needsClarification=true.
                 - For INTENT_COLLISION, add one follow-up disambiguation question in followups.
                 - If top intent is clear, set intent to best intent and confidence to best confidence.
                 - If user input is question-like (what/where/when/why/how/which/who/help/details/required/needed),
                   keep informational intents (like FAQ-style intents) in intentScores unless clearly impossible.
                 - When a domain/task intent and informational intent are both plausible for a question, keep both with close scores;
                   prefer INTENT_COLLISION instead of collapsing too early.
                 - Use only allowed intents.
                 - Do not hallucinate missing identifiers or facts.
                 - Keep state non-null when possible.', true, '2026-02-19 23:22:22.487');
INSERT INTO ce_config
(config_id, config_type, config_key, config_value, enabled, created_at)
VALUES(7, 'AgentIntentResolver', 'USER_PROMPT', ' Context:
                {{context}}

                Allowed intents:
                {{allowed_intents}}

                Potential intent collisions:
                {{intent_collision_candidates}}

                Current intent scores:
                {{intent_scores}}

                Previous clarification question (if any):
                {{pending_clarification}}

                User input:
                {{user_input}}

                Return JSON in the required schema only.', true, '2026-02-19 23:22:22.487');
INSERT INTO ce_config
(config_id, config_type, config_key, config_value, enabled, created_at)
VALUES(8, 'AgentIntentCollisionResolver', 'SYSTEM_PROMPT', 'You are a workflow assistant handling ambiguous intent collisions.
Use followups first when present.
Ask one concise disambiguation question.
If followups is empty, ask user to choose from top intents.', true, '2026-02-10 10:15:54.230');
INSERT INTO ce_config
(config_id, config_type, config_key, config_value, enabled, created_at)
VALUES(9, 'AgentIntentCollisionResolver', 'USER_PROMPT', '
User message:
{{user_input}}

Followups:
{{followups}}

Top intent scores:
{{intent_top3}}

Session:
{{session}}

Context:
{{context}}
', true, '2026-02-10 10:15:54.230');
INSERT INTO ce_config
(config_id, config_type, config_key, config_value, enabled, created_at)
VALUES(10, 'AgentIntentCollisionResolver', 'DERIVATION_HINT', 'When multiple intents have similar scores, derive a new intent to disambiguate.
                Consider followup questions, top intent scores, and conversation history.', true, '2026-02-10 10:15:54.230');



INSERT INTO ce_config
(config_id, config_type, config_key, config_value, enabled, created_at)
VALUES(11, 'DialogueActStep', 'SYSTEM_PROMPT', 'You are a dialogue-act classifier.
Return JSON only with:
{"dialogueAct":"AFFIRM|NEGATE|EDIT|RESET|QUESTION|NEW_REQUEST","confidence":0.0}', true, '2026-02-20 10:15:54.230');

INSERT INTO ce_config
(config_id, config_type, config_key, config_value, enabled, created_at)
VALUES(12, 'DialogueActStep', 'USER_PROMPT', 'User text:
%s', true, '2026-02-20 10:15:54.230');

INSERT INTO ce_config
(config_id, config_type, config_key, config_value, enabled, created_at)
VALUES(13, 'DialogueActStep', 'SCHEMA_PROMPT', '{
  "type":"object",
  "required":["dialogueAct","confidence"],
  "properties":{
    "dialogueAct":{"type":"string","enum":["AFFIRM","NEGATE","EDIT","RESET","QUESTION","NEW_REQUEST"]},
    "confidence":{"type":"number"}
  },
  "additionalProperties":false
}', true, '2026-02-20 10:15:54.230');


-- -----------------------------------------------------------------------------
-- ce_policy
-- -----------------------------------------------------------------------------
INSERT INTO ce_policy (policy_id, rule_type, pattern, response_text, priority, enabled, description)
VALUES
(1, 'REGEX', '(?i)\\b(ignore (all )?previous|system prompt|jailbreak)\\b',
 'I can only help with supported workflow requests.', 5, true, 'Basic prompt-injection guardrail text');

-- -----------------------------------------------------------------------------
-- ce_intent + classifiers
-- -----------------------------------------------------------------------------
INSERT INTO ce_intent (intent_code, description, priority, enabled, display_name, llm_hint)
VALUES
('GREETING', 'When user greets you', 15, true, 'GREETING', 'Reply with firm greetings'),
('FAQ', 'Answer informational questions from FAQ knowledge base', 10, true, 'FAQ', 'Informational queries and help questions'),
('CONNECTION_TRANSFER', 'Transfer electricity connection from one city to another', 20, true, 'Connection Transfer', 'Move/transfer/relocation request'),
('UNKNOWN', 'Fallback intent', 999, true, 'Unknown', 'Fallback when no intent matches');

INSERT INTO ce_intent_classifier (intent_code, rule_type, pattern, priority, enabled, description)
VALUES
('GREETING', 'REGEX', '(?i)\\b(hi|hello|hey|howdy|whats up)\\b', 15, true, 'GREETING regex matcher'),
('FAQ', 'REGEX', '(?i)\\b(what|how|help|faq|information|details|explain)\\b', 10, true, 'FAQ regex matcher'),
('CONNECTION_TRANSFER', 'REGEX', '(?i)\\b(move|transfer|shift|relocat(e|ion)|change city|connection transfer)\\b', 20, true, 'Connection transfer matcher');

-- -----------------------------------------------------------------------------
-- ce_output_schema (v2 schema extraction)
-- -----------------------------------------------------------------------------
INSERT INTO ce_output_schema (intent_code, state_code, json_schema, description, enabled, priority)
VALUES
('FAQ', 'IDLE',
 '{"type":"object","properties":{"question":{"type":"string"}}}'::jsonb,
 'FAQ user question schema', true, 1),
('CONNECTION_TRANSFER', 'COLLECT_INPUTS',
 '{
   "type":"object",
   "properties":{
     "customerId":{"type":"string"},
     "phone":{"type":"string"},
     "email":{"type":"string"},
     "sourceCity":{"type":"string"},
     "targetCity":{"type":"string"}
   },
   "required":["customerId","phone","email","sourceCity","targetCity"]
 }'::jsonb,
 'Connection transfer required fields', true, 1);

-- -----------------------------------------------------------------------------
-- ce_prompt_template
-- -----------------------------------------------------------------------------
INSERT INTO ce_prompt_template (intent_code, state_code, response_type, system_prompt, user_prompt, temperature, enabled)
VALUES
('FAQ', 'IDLE', 'TEXT',
 'You are a concise FAQ assistant. Answer directly and clearly.',
 'User question: {{user_input}}\nFAQ context: {{container_data}}\nReturn short helpful answer.',
 0.10, true),
('CONNECTION_TRANSFER', 'COLLECT_INPUTS', 'SCHEMA_JSON',
 'Extract only valid JSON matching given schema.',
 'User input: {{user_input}}\nSchema: {{schema}}\nContext: {{context}}\nReturn JSON only.',
 0.00, true),
('CONNECTION_TRANSFER', 'AWAITING_CONFIRMATION', 'TEXT',
 'You are a transaction confirmation assistant.',
 'Ask user to confirm execution in one short line. Context: {{context}}',
 0.00, true);

-- -----------------------------------------------------------------------------
-- ce_response
-- -----------------------------------------------------------------------------
INSERT INTO ce_response (intent_code, state_code, output_format, response_type, exact_text, derivation_hint, json_schema, priority, enabled, description)
VALUES
('FAQ', 'IDLE', 'TEXT', 'DERIVED', NULL,
 'Answer FAQ using available context and user question.', NULL, 10, true, 'FAQ derived text response'),
('CONNECTION_TRANSFER', 'IDLE', 'TEXT', 'EXACT',
 'Please share customerId, phone, email, source city, and target city to start transfer.',
 NULL, NULL, 10, true, 'Kick off transfer input collection'),
('CONNECTION_TRANSFER', 'COLLECT_INPUTS', 'TEXT', 'EXACT',
 'Please provide missing required fields: customerId, phone, email, sourceCity, targetCity.',
 NULL, NULL, 20, true, 'Missing field follow-up'),
('CONNECTION_TRANSFER', 'AWAITING_CONFIRMATION', 'TEXT', 'EXACT',
 'Do you want to move this connection right away?',
 NULL, NULL, 30, true, 'Awaiting user confirmation'),
('CONNECTION_TRANSFER', 'COMPLETED', 'TEXT', 'EXACT',
 'Connection transfer request submitted successfully.',
 NULL, NULL, 40, true, 'Transfer success response'),
(NULL, 'ANY', 'TEXT', 'EXACT',
 'Sorry, I did not understand that. Please rephrase.',
 NULL, NULL, 999, true, 'Global fallback response'),
('FAQ', 'IDLE', 'JSON', 'DERIVED',
 NULL,
 'Answer using FAQ JSON prompt and include confidence give output as JSON only.', '{"type": "object", "required": ["answer", "confidence"], "properties": {"state": {"type": "string"}, "answer": {"type": "string"}, "intent": {"type": "string"}, "confidence": {"type": "number"}, "matchedFaqIds": {"type": "array", "items": {"type": "number"}}}, "additionalProperties": false}'::jsonb, 1, true, NULL);

-- -----------------------------------------------------------------------------
-- ce_container_config (FAQ container mapping example)
-- Note: page/section/container ids should match your CCF data model.
-- -----------------------------------------------------------------------------
INSERT INTO ce_container_config (intent_code, state_code, page_id, section_id, container_id, input_param_name, priority, enabled)
VALUES
('FAQ', 'IDLE', 1, 1, 101, 'container_data', 1, true);

-- -----------------------------------------------------------------------------
-- ce_rule (v2 flow rules)
-- -----------------------------------------------------------------------------
INSERT INTO ce_rule (phase, intent_code, state_code, rule_type, match_pattern, "action", action_value, priority, enabled, description)
VALUES
('AGENT_POST_INTENT', 'CONNECTION_TRANSFER', 'IDLE', 'REGEX', '.*', 'SET_STATE', 'COLLECT_INPUTS', 10, true,
 'Bootstrap connection transfer into COLLECT_INPUTS'),
('PIPELINE_RULES', 'CONNECTION_TRANSFER', 'ANY', 'REGEX', '(?i)^(edit|revise|change)$', 'SET_STATE', 'COLLECT_INPUTS', 20, true,
 'Edit command returns to COLLECT_INPUTS'),
('PIPELINE_RULES', 'CONNECTION_TRANSFER', 'COLLECT_INPUTS', 'JSON_PATH',
 '$[?(@.state == ''COLLECT_INPUTS'' && @.customerId && @.phone && @.email && @.sourceCity && @.targetCity)]',
 'SET_STATE', 'AWAITING_CONFIRMATION', 100, true,
 'All required transfer fields collected'),
('PIPELINE_RULES', 'CONNECTION_TRANSFER', 'AWAITING_CONFIRMATION', 'JSON_PATH',
 '$[?(@.state == ''AWAITING_CONFIRMATION'' && (@.pending_action_result == ''EXECUTED'' || @.inputParams.pending_action_result == ''EXECUTED''))]',
 'SET_STATE', 'COMPLETED', 200, true,
 'After pending action execution move to COMPLETED'),
('PIPELINE_RULES', 'FAQ', 'JSON_PATH', '$[?(@.hasContainerData == true)]', 'SET_TASK', 'faqRuleTask:injectContainerData', 71, true, NULL);

-- -----------------------------------------------------------------------------
-- ce_pending_action (v2 catalog)
-- -----------------------------------------------------------------------------
INSERT INTO ce_pending_action (intent_code, state_code, action_key, bean_name, method_names, priority, enabled, description)
VALUES
('CONNECTION_TRANSFER', 'AWAITING_CONFIRMATION', 'FINALIZE_CONNECTION_TRANSFER', 'pendingActionTask', 'withUserConfirmationOfMoveConnectionDoSomething', 1, true,
 'Demo finalize action for connection transfer');

-- -----------------------------------------------------------------------------
-- MCP tool setup (optional but seeded)
-- -----------------------------------------------------------------------------
INSERT INTO ce_mcp_tool (tool_id, tool_code, tool_group, intent_code, state_code, enabled, description)
VALUES
(1, 'postgres.schema', 'DB', NULL, NULL, true, 'List table/column metadata'),
(2, 'postgres.query', 'DB', NULL, NULL, true, 'Run safe parameterized SQL query');

INSERT INTO ce_mcp_db_tool (tool_id, dialect, sql_template, param_schema, safe_mode, max_rows, allowed_identifiers)
VALUES
(1, 'POSTGRES',
 'SELECT table_schema, table_name, column_name, data_type FROM information_schema.columns WHERE table_schema = :schema ORDER BY table_name, ordinal_position LIMIT :max_rows',
 '{"type":"object","properties":{"schema":{"type":"string"},"max_rows":{"type":"integer"}},"required":["schema"],"additionalProperties":false}'::jsonb,
 true, 200, '["information_schema.columns"]'::jsonb),
(2, 'POSTGRES',
 'SELECT * FROM {{table}} LIMIT :max_rows',
 '{"type":"object","properties":{"table":{"type":"string"},"max_rows":{"type":"integer"}},"required":["table"],"additionalProperties":false}'::jsonb,
 true, 100, '["zp_faq","ce_intent","ce_response","ce_rule"]'::jsonb);

-- -----------------------------------------------------------------------------
-- FAQ knowledge rows
-- -----------------------------------------------------------------------------
INSERT INTO zp_faq (category, question, answer, tags, enabled, priority)
VALUES
('GENERAL', 'How do I transfer my electricity connection?',
 'Share customerId, phone, email, source city, and target city. Then confirm to submit transfer.',
 'connection,transfer,relocation', true, 10),
('GENERAL', 'How long does connection transfer take?',
 'Typical processing time is 2 to 5 business days depending on verification and local service availability.',
 'sla,time,transfer', true, 20);

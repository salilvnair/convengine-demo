INSERT INTO ce_verbose
(verbose_id, intent_code, state_code, step_match, step_value, determinant, rule_id, tool_code, message, error_message, priority, enabled, created_at)
VALUES(1, 'ANY', 'ANY', 'REGEX', '.*Step$', 'STEP_ENTER', NULL, NULL, 'Agent is processing your request.', 'Failed while starting step execution.', 100, true, '2026-02-26 17:06:02.369');
INSERT INTO ce_verbose
(verbose_id, intent_code, state_code, step_match, step_value, determinant, rule_id, tool_code, message, error_message, priority, enabled, created_at)
VALUES(2, 'ANY', 'ANY', 'REGEX', '.*Step$', 'STEP_EXIT', NULL, NULL, 'Agent is thinking...', 'Step completed with issues.', 100, true, '2026-02-26 17:06:02.369');
INSERT INTO ce_verbose
(verbose_id, intent_code, state_code, step_match, step_value, determinant, rule_id, tool_code, message, error_message, priority, enabled, created_at)
VALUES(3, 'ANY', 'ANY', 'EXACT', 'RulesStep', 'RULE_MATCH', NULL, NULL, 'Applying matching rule...', 'Rule evaluation failed.', 20, true, '2026-02-26 17:06:02.369');
INSERT INTO ce_verbose
(verbose_id, intent_code, state_code, step_match, step_value, determinant, rule_id, tool_code, message, error_message, priority, enabled, created_at)
VALUES(4, 'ANY', 'ANY', 'EXACT', 'McpToolStep', 'MCP_TOOL_CALL', NULL, 'loan.credit.rating.check', 'Checking credit rating from credit union.', 'Unable to fetch credit rating at the moment.', 10, true, '2026-02-26 17:06:02.369');
INSERT INTO ce_verbose
(verbose_id, intent_code, state_code, step_match, step_value, determinant, rule_id, tool_code, message, error_message, priority, enabled, created_at)
VALUES(5, 'ANY', 'ANY', 'EXACT', 'McpToolStep', 'MCP_TOOL_CALL', NULL, 'loan.credit.fraud.check', 'Running fraud verification.', 'Fraud verification failed. Please retry shortly.', 20, true, '2026-02-26 17:06:02.369');
INSERT INTO ce_verbose
(verbose_id, intent_code, state_code, step_match, step_value, determinant, rule_id, tool_code, message, error_message, priority, enabled, created_at)
VALUES(6, 'ANY', 'ANY', 'EXACT', 'McpToolStep', 'MCP_TOOL_CALL', NULL, 'loan.debt.credit.summary', 'Analyzing debt-to-income and available credit.', 'Unable to fetch debt and credit summary right now.', 30, true, '2026-02-26 17:06:02.369');
INSERT INTO ce_verbose
(verbose_id, intent_code, state_code, step_match, step_value, determinant, rule_id, tool_code, message, error_message, priority, enabled, created_at)
VALUES(7, 'ANY', 'ANY', 'EXACT', 'McpToolStep', 'MCP_TOOL_CALL', NULL, 'loan.application.submit', 'Submitting loan application.', 'Loan submission failed. Please retry in a few moments.', 40, true, '2026-02-26 17:06:02.369');
INSERT INTO ce_verbose
(verbose_id, intent_code, state_code, step_match, step_value, determinant, rule_id, tool_code, message, error_message, priority, enabled, created_at)
VALUES(8, 'ANY', 'ANY', 'EXACT', 'McpToolStep', 'MCP_FINAL_ANSWER', NULL, NULL, 'Loan workflow completed.', 'Loan workflow hit an error.', 90, true, '2026-02-26 17:06:02.369');
INSERT INTO ce_verbose
(verbose_id, intent_code, state_code, step_match, step_value, determinant, rule_id, tool_code, message, error_message, priority, enabled, created_at)
VALUES(9, 'ANY', 'ANY', 'REGEX', '.*Step$', 'STEP_ERROR', NULL, NULL, 'Step execution failed.', 'Step execution failed.', 5, true, '2026-02-26 17:06:02.369');
INSERT INTO ce_verbose
(verbose_id, intent_code, state_code, step_match, step_value, determinant, rule_id, tool_code, message, error_message, priority, enabled, created_at)
VALUES(10, 'ANY', 'ANY', 'EXACT', 'AgentIntentResolver', 'AGENT_INTENT_START', NULL, NULL, 'Analyzing user intent...', 'Unable to start intent analysis.', 30, true, '2026-02-26 17:06:02.369');
INSERT INTO ce_verbose
(verbose_id, intent_code, state_code, step_match, step_value, determinant, rule_id, tool_code, message, error_message, priority, enabled, created_at)
VALUES(11, 'ANY', 'ANY', 'EXACT', 'AgentIntentResolver', 'AGENT_INTENT_ACCEPTED', NULL, NULL, 'Intent resolved successfully.', 'Intent resolution failed.', 30, true, '2026-02-26 17:06:02.369');
INSERT INTO ce_verbose
(verbose_id, intent_code, state_code, step_match, step_value, determinant, rule_id, tool_code, message, error_message, priority, enabled, created_at)
VALUES(12, 'ANY', 'ANY', 'EXACT', 'AgentIntentResolver', 'AGENT_INTENT_COLLISION', NULL, NULL, 'Intent ambiguity detected. Preparing clarification.', 'Intent disambiguation failed.', 20, true, '2026-02-26 17:06:02.369');
INSERT INTO ce_verbose
(verbose_id, intent_code, state_code, step_match, step_value, determinant, rule_id, tool_code, message, error_message, priority, enabled, created_at)
VALUES(13, 'ANY', 'ANY', 'EXACT', 'AgentIntentResolver', 'AGENT_INTENT_NEEDS_CLARIFICATION', NULL, NULL, 'Clarification is required before proceeding.', 'Could not prepare clarification.', 20, true, '2026-02-26 17:06:02.369');
INSERT INTO ce_verbose
(verbose_id, intent_code, state_code, step_match, step_value, determinant, rule_id, tool_code, message, error_message, priority, enabled, created_at)
VALUES(14, 'ANY', 'ANY', 'EXACT', 'AgentIntentResolver', 'AGENT_INTENT_REJECTED', NULL, NULL, 'Intent could not be finalized.', 'Intent resolution was rejected.', 10, true, '2026-02-26 17:06:02.369');
INSERT INTO ce_verbose
(verbose_id, intent_code, state_code, step_match, step_value, determinant, rule_id, tool_code, message, error_message, priority, enabled, created_at)
VALUES(15, 'ANY', 'ANY', 'EXACT', 'RuleActionResolverFactory', 'RULE_ACTION_RESOLVER_SELECTED', NULL, NULL, 'Applying matched rule action.', 'Failed to apply rule action.', 25, true, '2026-02-26 17:06:02.369');
INSERT INTO ce_verbose
(verbose_id, intent_code, state_code, step_match, step_value, determinant, rule_id, tool_code, message, error_message, priority, enabled, created_at)
VALUES(16, 'ANY', 'ANY', 'EXACT', 'RuleActionResolverFactory', 'RULE_ACTION_RESOLVER_NOT_FOUND', NULL, NULL, 'No rule action resolver found for this action.', 'No rule action resolver found.', 10, true, '2026-02-26 17:06:02.369');
INSERT INTO ce_verbose
(verbose_id, intent_code, state_code, step_match, step_value, determinant, rule_id, tool_code, message, error_message, priority, enabled, created_at)
VALUES(17, 'ANY', 'ANY', 'EXACT', 'ResponseTypeResolverFactory', 'RESPONSE_TYPE_RESOLVER_SELECTED', NULL, NULL, 'Selected response strategy.', 'Unable to select response strategy.', 25, true, '2026-02-26 17:06:02.369');
INSERT INTO ce_verbose
(verbose_id, intent_code, state_code, step_match, step_value, determinant, rule_id, tool_code, message, error_message, priority, enabled, created_at)
VALUES(18, 'ANY', 'ANY', 'EXACT', 'ResponseTypeResolverFactory', 'RESPONSE_TYPE_RESOLVER_NOT_FOUND', NULL, NULL, 'No response strategy matched.', 'No response type resolver found.', 10, true, '2026-02-26 17:06:02.369');
INSERT INTO ce_verbose
(verbose_id, intent_code, state_code, step_match, step_value, determinant, rule_id, tool_code, message, error_message, priority, enabled, created_at)
VALUES(19, 'ANY', 'ANY', 'EXACT', 'OutputFormatResolverFactory', 'OUTPUT_FORMAT_RESOLVER_SELECTED', NULL, NULL, 'Selected response output formatter.', 'Unable to select output formatter.', 25, true, '2026-02-26 17:06:02.369');
INSERT INTO ce_verbose
(verbose_id, intent_code, state_code, step_match, step_value, determinant, rule_id, tool_code, message, error_message, priority, enabled, created_at)
VALUES(20, 'ANY', 'ANY', 'EXACT', 'OutputFormatResolverFactory', 'OUTPUT_FORMAT_RESOLVER_NOT_FOUND', NULL, NULL, 'No output formatter matched.', 'No output format resolver found.', 10, true, '2026-02-26 17:06:02.369');


INSERT INTO ce_intent
(intent_code, description, priority, enabled, created_at, display_name, llm_hint)
VALUES('LOAN_APPLICATION', 'Loan application eligibility and submission workflow', 30, true, '2026-02-26 01:57:06.961', 'Loan Application', 'Handle loan checks and submission using MCP APIs in strict sequence.');
INSERT INTO ce_intent
(intent_code, description, priority, enabled, created_at, display_name, llm_hint)
VALUES('GREETING', 'When user greets you', 15, true, '2026-02-26 01:57:06.990', 'GREETING', 'Reply with firm greetings');
INSERT INTO ce_intent
(intent_code, description, priority, enabled, created_at, display_name, llm_hint)
VALUES('UNKNOWN', 'Fallback intent', 999, true, '2026-02-26 01:57:06.990', 'Unknown', 'Fallback when no intent matches');
INSERT INTO ce_intent_classifier
(classifier_id, intent_code, state_code, rule_type, pattern, priority, enabled, description)
VALUES(9, 'GREETING', 'IDLE', 'REGEX', '(?i)\b(hi|hello|hey|howdy)\b', 15, true, 'GREETING regex matcher');
INSERT INTO ce_intent_classifier
(classifier_id, intent_code, state_code, rule_type, pattern, priority, enabled, description)
VALUES(8, 'LOAN_APPLICATION', 'ELIGIBILITY_GATE', 'REGEX', '(?i)\b(loan|apply loan|personal loan|home loan|eligibility|credit score|loan application)\b', 30, true, 'Loan application classifier');
INSERT INTO ce_output_schema
(schema_id, intent_code, state_code, json_schema, description, enabled, priority)
VALUES(9, 'LOAN_APPLICATION', 'ELIGIBILITY_GATE', '{"type": "object", "required": ["customerId", "requestedAmount", "tenureMonths"], "properties": {"customerId": {"type": "string", "maxLength": 64, "minLength": 3}, "tenureMonths": {"type": "integer", "maximum": 480, "minimum": 6}, "requestedAmount": {"type": "number", "maximum": 50000000, "minimum": 1000}}, "additionalProperties": false}'::jsonb, 'Loan application required fields for MCP chain', true, 1);


INSERT INTO ce_prompt_template
(template_id, intent_code, state_code, response_type, system_prompt, user_prompt, temperature, enabled, created_at)
VALUES(16, 'LOAN_APPLICATION', 'COMPLETED', 'TEXT', 'You are a strict loan decision summarizer.', 'Context JSON:\n{{context}}\n\nUse context.mcp.finalAnswer as primary final answer. Use context.mcp.observations only to validate details.', 0.00, true, '2026-02-26 01:57:06.965');
INSERT INTO ce_prompt_template
(template_id, intent_code, state_code, response_type, system_prompt, user_prompt, temperature, enabled, created_at)
VALUES(23, 'LOAN_APPLICATION', 'ELIGIBILITY_GATE', 'TEXT', 'You are a strict loan decision summarizer.', 'Context JSON:\n{{context}}\n\nIf schema is incomplete, respond with exactly which required fields are missing using {{missing_fields}} and ask only for those fields. If schema is complete, read context.mcp.observations and context.mcp.finalAnswer when present. Mention rating, fraud flag, dti, availableCredit, and applicationId when available.', 0.00, true, '2026-02-26 18:15:02.248');
INSERT INTO ce_prompt_template
(template_id, intent_code, state_code, response_type, system_prompt, user_prompt, temperature, enabled, created_at)
VALUES(24, 'LOAN_APPLICATION', 'ELIGIBILITY_GATE', 'SCHEMA_JSON', 'You are a strict structured extractor for loan application fields.', 'User input:\n{{user_input}}\n\nContext JSON:\n{{context}}\n\nExtract only: customerId, requestedAmount, tenureMonths.\nFor each field: return actual value only if explicitly present in input/context; otherwise return null.\nNever invent values.\nNever use placeholders like "", 0, or 0.0 for missing values.\nReturn valid JSON only.', 0.00, true, '2026-02-26 21:00:33.582');
INSERT INTO ce_response
(response_id, intent_code, state_code, output_format, response_type, exact_text, derivation_hint, json_schema, priority, enabled, description, created_at)
VALUES(21, 'GREETING', 'ANY', 'TEXT', 'EXACT', 'Hi 😁, How can I help you today?', NULL, NULL, 50, true, 'Global greetings response', '2026-02-26 01:57:06.992');
INSERT INTO ce_response
(response_id, intent_code, state_code, output_format, response_type, exact_text, derivation_hint, json_schema, priority, enabled, description, created_at)
VALUES(20, 'LOAN_APPLICATION', 'COMPLETED', 'TEXT', 'DERIVED', NULL, 'Use context.mcp.finalAnswer as primary answer. Validate wording with context.mcp.observations and include evidence fields when present.', NULL, 30, true, 'Loan completed response derived from MCP final answer', '2026-02-26 01:57:06.967');
INSERT INTO ce_response
(response_id, intent_code, state_code, output_format, response_type, exact_text, derivation_hint, json_schema, priority, enabled, description, created_at)
VALUES(23, 'LOAN_APPLICATION', 'IDLE', 'TEXT', 'DERIVED', NULL, 'If missing_fields is not empty, clearly list only the missing required fields from {{missing_fields}}. Ask user to provide them to proceed. If no fields are missing, proceed with normal loan eligibility summary.', NULL, 10, true, 'Loan intake derived prompt with explicit missing required fields', '2026-02-26 18:15:11.557');
INSERT INTO ce_response
(response_id, intent_code, state_code, output_format, response_type, exact_text, derivation_hint, json_schema, priority, enabled, description, created_at)
VALUES(24, 'LOAN_APPLICATION', 'ELIGIBILITY_GATE', 'TEXT', 'DERIVED', NULL, 'If missing_fields is not empty, clearly list only missing required fields from {{missing_fields}} and do not claim processing/submission. Otherwise use context.mcp.observations and context.mcp.finalAnswer to explain decision. Reject on low rating/fraud/poor profile. Include applicationId when submitted.', NULL, 20, true, 'Loan decision derived from MCP chain', '2026-02-26 18:15:11.557');
INSERT INTO ce_response
(response_id, intent_code, state_code, output_format, response_type, exact_text, derivation_hint, json_schema, priority, enabled, description, created_at)
VALUES(22, 'UNKNOWN', 'ANY', 'TEXT', 'EXACT', 'Sorry, I did not understand that. Please rephrase.', NULL, NULL, 999, true, 'Global fallback response', '2026-02-26 01:57:06.992');


INSERT INTO ce_rule
(rule_id, phase, intent_code, state_code, rule_type, match_pattern, "action", action_value, priority, enabled, description, created_at)
VALUES(28, 'POST_AGENT_INTENT', 'LOAN_APPLICATION', 'UNKNOWN', 'REGEX', '.*', 'SET_STATE', 'ELIGIBILITY_GATE', 60, true, 'Move LOAN_APPLICATION into ELIGIBILITY_GATE when classifier state is UNKNOWN', '2026-02-26 01:57:06.973');
INSERT INTO ce_rule
(rule_id, phase, intent_code, state_code, rule_type, match_pattern, "action", action_value, priority, enabled, description, created_at)
VALUES(29, 'POST_AGENT_INTENT', 'LOAN_APPLICATION', 'IDLE', 'REGEX', '.*', 'SET_STATE', 'ELIGIBILITY_GATE', 61, true, 'Move LOAN_APPLICATION into ELIGIBILITY_GATE when state is IDLE', '2026-02-26 01:57:06.973');
INSERT INTO ce_rule
(rule_id, phase, intent_code, state_code, rule_type, match_pattern, "action", action_value, priority, enabled, description, created_at)
VALUES(30, 'POST_AGENT_MCP', 'LOAN_APPLICATION', 'ELIGIBILITY_GATE', 'JSON_PATH', '$[?(@.context.mcp.finalAnswer != null && @.context.mcp.finalAnswer != '''')]', 'SET_STATE', 'COMPLETED', 62, true, 'Move LOAN_APPLICATION to COMPLETED when context.mcp.finalAnswer exists', '2026-02-26 01:57:06.973');
INSERT INTO ce_rule
(rule_id, phase, intent_code, state_code, rule_type, match_pattern, "action", action_value, priority, enabled, description, created_at)
VALUES(32, 'PRE_RESPONSE_RESOLUTION', 'LOAN_APPLICATION', 'ANY', 'REGEX', '(?i)\b(reset|restart|start over)\b', 'SET_STATE', 'IDLE', 81, true, 'Allow reset for loan flow', '2026-02-26 01:57:06.973');


INSERT INTO ce_mcp_planner
(planner_id, intent_code, state_code, system_prompt, user_prompt, enabled, created_at)
VALUES(5201, 'ANY', 'ANY', 'You are an MCP planning agent inside ConvEngine. Decide whether to CALL_TOOL or ANSWER. Be conservative, safe, and do not hallucinate missing data. Return JSON only.', 'User input:\n{{user_input}}\n\nContext JSON:\n{{context}}\n\nAvailable MCP tools:\n{{mcp_tools}}\n\nExisting MCP observations:\n{{mcp_observations}}\n\nReturn strict JSON:\n{\n  "action":"CALL_TOOL" | "ANSWER",\n  "tool_code":"<tool_code_or_null>",\n  "args":{},\n  "answer":"<text_or_null>"\n}', true, '2026-02-26 01:57:06.977');
INSERT INTO ce_mcp_planner
(planner_id, intent_code, state_code, system_prompt, user_prompt, enabled, created_at)
VALUES(5202, 'LOAN_APPLICATION', 'ELIGIBILITY_GATE', 'You are an MCP planning agent for a loan application workflow.\nYou MUST follow tool order:\n1) loan.credit.rating.check\n2) If creditRating <= 750 => ANSWER reject\n3) Else loan.credit.fraud.check\n4) If flagged=true => ANSWER reject\n5) Else loan.debt.credit.summary\n6) If dti > 0.65 or availableCredit < requestedAmount*0.15 => ANSWER reject/manual-review\n7) Else loan.application.submit\n8) ANSWER with applicationId.\nReturn JSON only. Never invent unknown values.', 'User input:\n{{user_input}}\n\nContext JSON:\n{{context}}\n\nAvailable MCP tools:\n{{mcp_tools}}\n\nExisting MCP observations:\n{{mcp_observations}}\n\nReturn strict JSON:\n{\n  "action":"CALL_TOOL" | "ANSWER",\n  "tool_code":"<tool_code_or_null>",\n  "args":{},\n  "answer":"<text_or_null>"\n}', true, '2026-02-26 01:57:06.977');


INSERT INTO ce_mcp_tool
(tool_id, tool_code, tool_group, intent_code, state_code, enabled, description, created_at)
VALUES(9201, 'loan.credit.rating.check', 'HTTP_API', 'LOAN_APPLICATION', 'ELIGIBILITY_GATE', true, 'Step 1: check customer credit rating from credit union API', '2026-02-26 01:57:06.975');
INSERT INTO ce_mcp_tool
(tool_id, tool_code, tool_group, intent_code, state_code, enabled, description, created_at)
VALUES(9202, 'loan.credit.fraud.check', 'HTTP_API', 'LOAN_APPLICATION', 'ELIGIBILITY_GATE', true, 'Step 2: if rating > 750, verify customer against fraud API', '2026-02-26 01:57:06.975');
INSERT INTO ce_mcp_tool
(tool_id, tool_code, tool_group, intent_code, state_code, enabled, description, created_at)
VALUES(9203, 'loan.debt.credit.summary', 'HTTP_API', 'LOAN_APPLICATION', 'ELIGIBILITY_GATE', true, 'Step 3: if fraud clear, fetch debt/credit summary', '2026-02-26 01:57:06.975');
INSERT INTO ce_mcp_tool
(tool_id, tool_code, tool_group, intent_code, state_code, enabled, description, created_at)
VALUES(9204, 'loan.application.submit', 'HTTP_API', 'LOAN_APPLICATION', 'ELIGIBILITY_GATE', true, 'Step 4: if profile healthy, submit final loan application', '2026-02-26 01:57:06.975');
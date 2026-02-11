INSERT INTO ce_intent
(intent_code, description, priority, enabled, created_at, display_name, llm_hint)
VALUES('FAQ', 'Answer questions from zp_faq', 10, true, '2026-01-15 00:43:24.057', 'FAQ', NULL);
INSERT INTO ce_intent
(intent_code, description, priority, enabled, created_at, display_name, llm_hint)
VALUES('FAQ_CONFIRMATION', 'FAQ User confirmation intent', 100, true, '2026-01-17 00:04:11.051', NULL, 'Prefer this intent over CONFIRMATION when the conversation context is about FAQ resolution');



INSERT INTO ce_intent_classifier
(classifier_id, intent_code, rule_type, pattern, priority, enabled, description)
VALUES(2, 'FAQ', 'REGEX', '\\b(how do i|how to|what is|where is|can you help|help me)\\b', 10, true, NULL);


INSERT INTO ce_prompt_template
(template_id, intent_code, response_type, system_prompt, user_prompt, temperature, enabled, created_at, state_code)
VALUES(19, 'FAQ_CLARIFICATION', 'JSON', '
You are a clarification resolution assistant previous bot answer has clarificationQuestion extract that and set as the answer.

Rules:
- You MUST use the conversation history to resolve ambiguity.
- The user already confirmed intent earlier.
- Do NOT repeat old questions.

You MUST return valid JSON only.
No explanations.
', '
Conversation history (latest first):
{{conversation_history}}

User message:
{{user_input}}

Return JSON EXACTLY in this format:
{
  "answer": "<final answer or clarification question>",
  "confidence": 0.0,
  "matchedFaqIds": [],
  "needsClarification": false
}
', 0.00, true, '2026-01-16 16:54:09.188', NULL);
INSERT INTO ce_prompt_template
(template_id, intent_code, response_type, system_prompt, user_prompt, temperature, enabled, created_at, state_code)
VALUES(18, 'FAQ', 'JSON', '
You are a clarification resolution assistant.

Rules:
- You MUST use the conversation history to resolve ambiguity.
- The user already confirmed intent earlier.
- Do NOT repeat old questions.
- If ambiguity is resolved, answer directly.
- If still ambiguous, ask ONLY ONE short clarification question.
- Set needsClarification=true only if absolutely required.
- Confidence must reflect certainty.

You MUST return valid JSON only.
No explanations.
', '
Conversation history (latest first):
{{conversation_history}}

User message:
{{user_input}}

FAQ data:
{{container_data}}

Return JSON EXACTLY in this format:
{
  "answer": "<final answer or clarification question>",
  "confidence": 0.0,
  "matchedFaqIds": [],
  "needsClarification": false
}
', 0.00, true, '2026-01-16 16:54:09.188', NULL);


INSERT INTO ce_response
(response_id, intent_code, state_code, output_format, response_type, exact_text, derivation_hint, json_schema, priority, enabled, description, created_at)
VALUES(1, 'GREETING', 'IDLE', 'TEXT', 'EXACT', 'Hi, I am Conv Assistant. How can I help you?', NULL, NULL, 1, true, NULL, '2025-12-31 14:58:38.418');
INSERT INTO ce_response
(response_id, intent_code, state_code, output_format, response_type, exact_text, derivation_hint, json_schema, priority, enabled, description, created_at)
VALUES(2, NULL, 'ANY', 'TEXT', 'EXACT', 'Sorry, I did not understand that. Can you rephrase?', NULL, NULL, 999, true, NULL, '2025-12-31 14:58:38.447');
INSERT INTO ce_response
(response_id, intent_code, state_code, output_format, response_type, exact_text, derivation_hint, json_schema, priority, enabled, description, created_at)
VALUES(3, 'FAQ', 'IDLE', 'JSON', 'DERIVED', NULL, 'Answer using FAQ JSON prompt and include confidence give output as JSON only.', '{"type": "object", "required": ["answer", "confidence"], "properties": {"state": {"type": "string"}, "answer": {"type": "string"}, "intent": {"type": "string"}, "confidence": {"type": "number"}, "matchedFaqIds": {"type": "array", "items": {"type": "number"}}}, "additionalProperties": false}'::jsonb, 1, true, NULL, '2026-01-15 00:45:04.988');
INSERT INTO ce_response
(response_id, intent_code, state_code, output_format, response_type, exact_text, derivation_hint, json_schema, priority, enabled, description, created_at)
VALUES(17, 'FAQ_CLARIFICATION', 'IDLE', 'JSON', 'DERIVED', NULL, 'Answer using agent intent clarification question', '{"type": "object", "required": ["answer", "confidence"], "properties": {"state": {"type": "string"}, "answer": {"type": "string"}, "intent": {"type": "string"}, "confidence": {"type": "number"}, "matchedFaqIds": {"type": "array", "items": {"type": "number"}}}, "additionalProperties": false}'::jsonb, 1000, true, NULL, '2026-01-15 00:45:04.988');


INSERT INTO ce_rule
(rule_id, intent_code, rule_type, match_pattern, "action", action_value, priority, enabled, description, created_at)
VALUES(16, 'FAQ', 'JSON_PATH', '$[?(@.confidence >= 0.6)]', 'SET_STATE', 'IDLE', 20, true, 'Clarification resolved → return to IDLE', '2026-01-16 16:49:46.331');
INSERT INTO ce_rule
(rule_id, intent_code, rule_type, match_pattern, "action", action_value, priority, enabled, description, created_at)
VALUES(17, 'FAQ', 'JSON_PATH', '$[?(@.confidence >= 0.6)]', 'SET_INTENT', 'FAQ', 30, true, 'Ensure FAQ intent after clarification', '2026-01-16 16:49:58.650');
INSERT INTO ce_rule
(rule_id, intent_code, rule_type, match_pattern, "action", action_value, priority, enabled, description, created_at)
VALUES(18, 'FAQ_CONFIRMATION', 'REGEX', '.*', 'SET_INTENT', 'FAQ', 50, true, 'Ensure FAQ intent after clarification', '2025-12-31 14:58:38.363');
INSERT INTO ce_rule
(rule_id, intent_code, rule_type, match_pattern, "action", action_value, priority, enabled, description, created_at)
VALUES(19, 'FAQ', 'JSON_PATH', '$[?(@.needsClarification == true)]', 'SET_INTENT', 'FAQ_CLARIFICATION', 70, true, NULL, '2026-01-16 16:49:58.650');
INSERT INTO ce_rule
(rule_id, intent_code, rule_type, match_pattern, "action", action_value, priority, enabled, description, created_at)
VALUES(23, NULL, 'JSON_PATH', '$[?(@.state == "INTENT_COLLISION")]', 'SET_JSON', 'followups:$.followups', 40, true, 'Expose followups from intent agent output', '2026-02-08 00:00:00.000');
INSERT INTO ce_rule
(rule_id, intent_code, rule_type, match_pattern, "action", action_value, priority, enabled, description, created_at)
VALUES(24, NULL, 'JSON_PATH', '$[?(@.state == "INTENT_COLLISION")]', 'GET_SESSION', 'session', 41, true, 'Expose session snapshot for intent collision', '2026-02-08 00:00:00.000');
INSERT INTO ce_rule
(rule_id, intent_code, rule_type, match_pattern, "action", action_value, priority, enabled, description, created_at)
VALUES(25, NULL, 'JSON_PATH', '$[?(@.state == "INTENT_COLLISION")]', 'GET_CONTEXT', 'context', 42, true, 'Expose context for intent collision', '2026-02-08 00:00:00.000');


INSERT INTO zp_container_query_info
(container_query_id, container_id, query_string, count_query_string, pagination_query_string, query_params)
VALUES(5001, 101, '
SELECT
    faq_id,
    question,
    answer,
    1 - (embedding <=> CAST(:faqQueryEmbedding AS vector)) AS score
FROM zp_faq
WHERE enabled = true
ORDER BY embedding <=> CAST(:faqQueryEmbedding AS vector)
    limit 10
    ', NULL, NULL, 'faqQueryEmbedding');
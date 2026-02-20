CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS vector;

CREATE SCHEMA IF NOT EXISTS v2;
SET search_path TO v2, public;

CREATE TABLE IF NOT EXISTS ce_config (
    config_id int4 NOT NULL,
    config_type text NOT NULL,
    config_key text NOT NULL,
    config_value text NOT NULL,
    enabled bool DEFAULT true NOT NULL,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT ce_config_pkey PRIMARY KEY (config_id)
);
CREATE UNIQUE INDEX IF NOT EXISTS ux_ce_config_type_key ON ce_config USING btree (config_type, config_key);

CREATE TABLE IF NOT EXISTS ce_container_config (
    id bigserial NOT NULL,
    intent_code text NOT NULL,
    state_code text NOT NULL,
    page_id int4 NOT NULL,
    section_id int4 NOT NULL,
    container_id int4 NOT NULL,
    input_param_name text NOT NULL,
    priority int4 DEFAULT 1 NOT NULL,
    enabled bool DEFAULT true NOT NULL,
    created_at timestamptz DEFAULT now() NOT NULL,
    CONSTRAINT ce_validation_config_pkey PRIMARY KEY (id)
);
CREATE INDEX IF NOT EXISTS idx_ce_validation_config_lookup ON ce_container_config USING btree (intent_code, state_code, enabled, priority);

CREATE TABLE IF NOT EXISTS ce_conversation (
    conversation_id uuid DEFAULT uuid_generate_v4() NOT NULL,
    status text NOT NULL,
    intent_code text NULL,
    state_code text NOT NULL,
    context_json jsonb DEFAULT '{}'::jsonb NOT NULL,
    last_user_text text NULL,
    last_assistant_json jsonb NULL,
    input_params_json jsonb DEFAULT '{}'::jsonb NOT NULL,
    created_at timestamptz DEFAULT now() NOT NULL,
    updated_at timestamptz DEFAULT now() NOT NULL,
    CONSTRAINT ce_conversation_pkey PRIMARY KEY (conversation_id)
);
CREATE INDEX IF NOT EXISTS idx_ce_conversation_status ON ce_conversation USING btree (status);
CREATE INDEX IF NOT EXISTS idx_ce_conversation_updated ON ce_conversation USING btree (updated_at);

CREATE TABLE IF NOT EXISTS ce_intent (
    intent_code text NOT NULL,
    description text NOT NULL,
    priority int4 DEFAULT 100 NOT NULL,
    enabled bool DEFAULT true NOT NULL,
    created_at timestamptz DEFAULT now() NOT NULL,
    display_name text NULL,
    llm_hint text NULL,
    CONSTRAINT ce_intent_pkey PRIMARY KEY (intent_code)
);
CREATE INDEX IF NOT EXISTS ix_ce_intent_enabled_priority ON ce_intent USING btree (enabled, priority, intent_code);

CREATE TABLE IF NOT EXISTS ce_intent_classifier (
    classifier_id bigserial NOT NULL,
    intent_code text NOT NULL,
    rule_type text NOT NULL,
    pattern text NOT NULL,
    priority int4 NOT NULL,
    enabled bool DEFAULT true,
    description text NULL,
    CONSTRAINT ce_intent_classifier_pkey PRIMARY KEY (classifier_id)
);

CREATE TABLE IF NOT EXISTS ce_llm_call_log (
    llm_call_id bigserial NOT NULL,
    conversation_id uuid NOT NULL,
    intent_code text NULL,
    state_code text NULL,
    provider text NOT NULL,
    model text NOT NULL,
    temperature numeric(3,2) NULL,
    prompt_text text NOT NULL,
    user_context text NOT NULL,
    response_text text NULL,
    success bool NOT NULL,
    error_message text NULL,
    created_at timestamptz DEFAULT now() NOT NULL,
    CONSTRAINT ce_llm_call_log_pkey PRIMARY KEY (llm_call_id)
);
CREATE INDEX IF NOT EXISTS idx_ce_llm_log_conversation ON ce_llm_call_log USING btree (conversation_id);
CREATE INDEX IF NOT EXISTS idx_ce_llm_log_intent_state ON ce_llm_call_log USING btree (intent_code, state_code);

CREATE TABLE IF NOT EXISTS ce_mcp_tool (
    tool_id bigserial NOT NULL,
    intent_code TEXT,
    state_code TEXT,
    tool_code text NOT NULL,
    tool_group text NOT NULL,
    intent_code text NULL,
    state_code text NULL,
    enabled bool DEFAULT true NOT NULL,
    description text NULL,
    created_at timestamptz DEFAULT now() NOT NULL,
    CONSTRAINT ce_mcp_tool_pkey PRIMARY KEY (tool_id),
    CONSTRAINT ce_mcp_tool_tool_code_key UNIQUE (tool_code)
);
CREATE INDEX IF NOT EXISTS idx_ce_mcp_tool_enabled ON ce_mcp_tool USING btree (enabled, intent_code, state_code, tool_group, tool_code);

CREATE TABLE IF NOT EXISTS ce_output_schema (
    schema_id bigserial NOT NULL,
    intent_code text NOT NULL,
    state_code text NOT NULL,
    json_schema jsonb NOT NULL,
    description text NULL,
    enabled bool DEFAULT true,
    priority int4 NOT NULL,
    CONSTRAINT ce_output_schema_pkey PRIMARY KEY (schema_id)
);
CREATE INDEX IF NOT EXISTS idx_ce_output_schema_lookup ON ce_output_schema USING btree (intent_code, state_code, enabled, priority);

CREATE TABLE IF NOT EXISTS ce_policy (
    policy_id bigserial NOT NULL,
    rule_type text NOT NULL,
    pattern text NOT NULL,
    response_text text NOT NULL,
    priority int4 DEFAULT 10 NOT NULL,
    enabled bool DEFAULT true NOT NULL,
    description text NULL,
    created_at timestamptz DEFAULT now() NOT NULL,
    CONSTRAINT ce_policy_pkey PRIMARY KEY (policy_id)
);
CREATE INDEX IF NOT EXISTS idx_ce_policy_priority ON ce_policy USING btree (enabled, priority);

CREATE TABLE IF NOT EXISTS ce_prompt_template (
    template_id bigserial NOT NULL,
    intent_code text NULL,
    state_code text NULL,
    response_type text NOT NULL,
    system_prompt text NOT NULL,
    user_prompt text NOT NULL,
    temperature numeric(3,2) DEFAULT 0.0 NOT NULL,
    enabled bool DEFAULT true NOT NULL,
    created_at timestamptz DEFAULT now() NOT NULL,
    CONSTRAINT ce_prompt_template_pkey PRIMARY KEY (template_id)
);
CREATE INDEX IF NOT EXISTS idx_ce_prompt_template_lookup ON ce_prompt_template USING btree (response_type, intent_code, state_code, enabled);

CREATE TABLE IF NOT EXISTS ce_response (
    response_id bigserial NOT NULL,
    intent_code text NULL,
    state_code text NOT NULL,
    output_format text NOT NULL,
    response_type text NOT NULL,
    exact_text text NULL,
    derivation_hint text NULL,
    json_schema jsonb NULL,
    priority int4 DEFAULT 100 NOT NULL,
    enabled bool DEFAULT true NOT NULL,
    description text NULL,
    created_at timestamptz DEFAULT now() NOT NULL,
    CONSTRAINT ce_response_pkey PRIMARY KEY (response_id)
);
CREATE INDEX IF NOT EXISTS idx_ce_response_intent_state ON ce_response USING btree (intent_code, state_code, enabled, priority);
CREATE INDEX IF NOT EXISTS idx_ce_response_lookup ON ce_response USING btree (state_code, enabled, priority);

CREATE TABLE IF NOT EXISTS ce_rule (
    rule_id bigserial NOT NULL,
    phase text DEFAULT 'PIPELINE_RULES' NOT NULL,
    intent_code text NULL,
    state_code text NULL,
    rule_type text NOT NULL,
    match_pattern text NOT NULL,
    "action" text NOT NULL,
    action_value text NULL,
    priority int4 DEFAULT 100 NOT NULL,
    enabled bool DEFAULT true NOT NULL,
    description text NULL,
    created_at timestamptz DEFAULT now() NOT NULL,
    CONSTRAINT ce_rule_pkey PRIMARY KEY (rule_id)
);
CREATE INDEX IF NOT EXISTS idx_ce_rule_priority ON ce_rule USING btree (enabled, phase, state_code, priority);

CREATE TABLE IF NOT EXISTS ce_pending_action (
    pending_action_id bigserial NOT NULL,
    intent_code text NULL,
    state_code text NULL,
    action_key text NOT NULL,
    bean_name text NOT NULL,
    method_names text NOT NULL,
    priority int4 DEFAULT 100 NOT NULL,
    enabled bool DEFAULT true NOT NULL,
    description text NULL,
    created_at timestamptz DEFAULT now() NOT NULL,
    CONSTRAINT ce_pending_action_pkey PRIMARY KEY (pending_action_id)
);
CREATE INDEX IF NOT EXISTS idx_ce_pending_action_lookup ON ce_pending_action USING btree (enabled, action_key, intent_code, state_code, priority);

CREATE TABLE IF NOT EXISTS ce_validation_snapshot (
    snapshot_id bigserial NOT NULL,
    conversation_id uuid NOT NULL,
    intent_code varchar(64) NULL,
    state_code varchar(64) NULL,
    validation_tables jsonb NULL,
    validation_decision text NULL,
    created_at timestamptz DEFAULT now() NOT NULL,
    schema_id int8 NULL,
    CONSTRAINT ce_validation_snapshot_pkey PRIMARY KEY (snapshot_id)
);
CREATE INDEX IF NOT EXISTS idx_ce_validation_snapshot_conv ON ce_validation_snapshot USING btree (conversation_id);

CREATE TABLE IF NOT EXISTS ce_audit (
    audit_id bigserial NOT NULL,
    conversation_id uuid NOT NULL,
    stage text NOT NULL,
    payload_json jsonb NOT NULL,
    created_at timestamptz DEFAULT now() NOT NULL,
    CONSTRAINT ce_audit_pkey PRIMARY KEY (audit_id),
    CONSTRAINT ce_audit_conversation_id_fkey FOREIGN KEY (conversation_id) REFERENCES ce_conversation(conversation_id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_ce_audit_conversation ON ce_audit USING btree (conversation_id, created_at DESC);

CREATE TABLE IF NOT EXISTS ce_conversation_history (
    history_id bigserial NOT NULL,
    conversation_id uuid NOT NULL,
    entry_type text NOT NULL,
    role text NOT NULL,
    stage text NOT NULL,
    content_text text NULL,
    payload_json jsonb NULL,
    created_at timestamptz DEFAULT now() NOT NULL,
    CONSTRAINT ce_conversation_history_pkey PRIMARY KEY (history_id),
    CONSTRAINT ce_conversation_history_conversation_id_fkey FOREIGN KEY (conversation_id) REFERENCES ce_conversation(conversation_id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_ce_conversation_history_conv ON ce_conversation_history USING btree (conversation_id, created_at DESC);

CREATE TABLE IF NOT EXISTS ce_mcp_db_tool (
    tool_id int8 NOT NULL,
    dialect text DEFAULT 'POSTGRES' NOT NULL,
    sql_template text NOT NULL,
    param_schema jsonb NOT NULL,
    safe_mode bool DEFAULT true NOT NULL,
    max_rows int4 DEFAULT 200 NOT NULL,
    created_at timestamptz DEFAULT now() NOT NULL,
    allowed_identifiers jsonb NULL,
    CONSTRAINT ce_mcp_db_tool_pkey PRIMARY KEY (tool_id),
    CONSTRAINT ce_mcp_db_tool_tool_id_fkey FOREIGN KEY (tool_id) REFERENCES ce_mcp_tool(tool_id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_ce_mcp_db_tool_dialect ON ce_mcp_db_tool USING btree (dialect);

CREATE TABLE IF NOT EXISTS zp_faq (
    faq_id bigserial NOT NULL,
    category text NULL,
    question text NOT NULL,
    answer text NOT NULL,
    tags text NULL,
    enabled bool DEFAULT true,
    priority int4 DEFAULT 100,
    created_at timestamptz DEFAULT now(),
    embedding vector NULL,
    CONSTRAINT zp_faq_pkey PRIMARY KEY (faq_id)
);
CREATE INDEX IF NOT EXISTS idx_zp_faq_embedding ON zp_faq USING ivfflat (embedding vector_cosine_ops) WITH (lists='100');
CREATE INDEX IF NOT EXISTS idx_zp_faq_enabled ON zp_faq USING btree (enabled);
CREATE INDEX IF NOT EXISTS idx_zp_faq_priority ON zp_faq USING btree (priority);

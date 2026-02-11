
CREATE TABLE zp_faq (
                        faq_id bigserial NOT NULL,
                        category text NULL,
                        question text NOT NULL,
                        answer text NOT NULL,
                        tags text NULL,
                        enabled bool DEFAULT true NULL,
                        priority int4 DEFAULT 100 NULL,
                        created_at timestamptz DEFAULT now() NULL,
                        embedding public.vector NULL,
                        CONSTRAINT zp_faq_pkey PRIMARY KEY (faq_id)
);
CREATE INDEX idx_zp_faq_embedding ON public.zp_faq USING ivfflat (embedding vector_cosine_ops) WITH (lists='100');
CREATE INDEX idx_zp_faq_enabled ON public.zp_faq USING btree (enabled);
CREATE INDEX idx_zp_faq_priority ON public.zp_faq USING btree (priority);
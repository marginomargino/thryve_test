CREATE TABLE thryve_test.dynamic_epoch (
  de_id                    BIGSERIAL PRIMARY KEY,
  de_timestamp_start       BIGINT,
  de_timestamp_end         BIGINT,
  de_created_at            BIGINT,
  de_user_id               TEXT,
  de_data_source_id        TEXT,
  de_dynamic_value_type    INT,
  de_value                 TEXT,
  de_value_type            TEXT)





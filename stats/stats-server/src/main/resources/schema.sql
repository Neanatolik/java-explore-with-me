DROP TABLE IF EXISTS hits CASCADE;
CREATE TABLE IF NOT EXISTS hits (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  app VARCHAR,
  uri VARCHAR,
  ip VARCHAR,
  timestamp VARCHAR,
  CONSTRAINT pk_hit PRIMARY KEY (id)
);
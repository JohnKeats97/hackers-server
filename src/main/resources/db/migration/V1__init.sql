CREATE EXTENSION IF NOT EXISTS intarray;

CREATE TABLE IF NOT EXISTS users
(
  email text NOT NULL,
  login text NOT NULL,
  password text NOT NULL,
  score integer NOT NULL DEFAULT 0,
--   tests INTEGER[] DEFAULT ARRAY[],
--   last_answer TIMESTAMP,
--   isEmail BOOLEAN DEFAULT false,
  CONSTRAINT unique_login PRIMARY KEY (login),
  CONSTRAINT unique_email UNIQUE (email)
);

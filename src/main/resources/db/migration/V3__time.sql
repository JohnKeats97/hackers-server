
CREATE TABLE IF NOT EXISTS time
(
  id SERIAL PRIMARY KEY NOT NULL,
  start_time text NOT NULL,
  end_time text NOT NULL
);

INSERT INTO time (start_time, end_time) VALUES ('2018-04-21', '2019-04-21');
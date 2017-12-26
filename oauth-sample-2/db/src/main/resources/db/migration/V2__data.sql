INSERT INTO users (id, name, password) VALUES
  (1, 'foo', 'foo-pass'),
  (2, 'bar', 'bar-pass'),
  (3, 'baz', 'baz-pass');

INSERT INTO messages(id, created, text, user_id) VALUES
  (1, PARSEDATETIME('2017-12-26 20:00:00', 'YYYY-MM-DD hh:mm:ss'), '初メッセージ', 1),
  (2, PARSEDATETIME('2017-12-26 20:20:00', 'YYYY-MM-DD hh:mm:ss'), 'ε٩( º∀º )۶з', 3),
  (3, PARSEDATETIME('2017-12-26 20:40:00', 'YYYY-MM-DD hh:mm:ss'), 'test', 2),
  (4, PARSEDATETIME('2017-12-26 21:00:00', 'YYYY-MM-DD hh:mm:ss'), 'テストじゃないよ', 1)
;

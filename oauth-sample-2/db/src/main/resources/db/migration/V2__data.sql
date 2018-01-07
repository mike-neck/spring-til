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

INSERT INTO user_authorities(user_id, authority) VALUES
  (1, 'READ_MESSAGE'),
  (1, 'CREATE_MESSAGE'),
  (1, 'DELETE_MESSAGE'),
  (1, 'READ_SELF_PROFILE'),
  (1, 'UPDATE_SELF_PROFILE'),
  (1, 'DELETE_SELF_PROFILE'),
  (1, 'READ_OTHER_PROFILE'),
  (1, 'UPDATE_OTHER_PROFILE'),
  (1, 'DELETE_OTHER_PROFILE'),

  (2, 'READ_MESSAGE'),
  (2, 'CREATE_MESSAGE'),
  (2, 'DELETE_MESSAGE'),
  (2, 'READ_SELF_PROFILE'),
  (2, 'UPDATE_SELF_PROFILE'),
  (2, 'DELETE_SELF_PROFILE'),

  (3, 'READ_MESSAGE'),
  (3, 'READ_SELF_PROFILE'),
;

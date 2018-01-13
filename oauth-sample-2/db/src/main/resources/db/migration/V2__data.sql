INSERT INTO users (id, name, password) VALUES
  (1, 'foo', 'foo-pass'),
  (2, 'bar', 'bar-pass'),
  (3, 'baz', 'baz-pass'),
  (4, 'qux', 'qux-pass'),
  (5, 'corge', 'corge-pass'),
  (6, 'grault', 'grault-pass'),
  (7, 'garply', 'garply-pass'),
  (8, 'waldo', 'waldo-pass');

INSERT INTO messages(id, created, text, user_id) VALUES
  (1, PARSEDATETIME('2017-12-26 20:00:00', 'YYYY-MM-DD hh:mm:ss'), 'middle-class rule', 7),
  (2, PARSEDATETIME('2017-12-26 20:11:00', 'YYYY-MM-DD hh:mm:ss'), 'quiet term', 4),
  (3, PARSEDATETIME('2017-12-26 20:22:00', 'YYYY-MM-DD hh:mm:ss'), 'tasty allowance', 3),
  (4, PARSEDATETIME('2017-12-26 20:33:00', 'YYYY-MM-DD hh:mm:ss'), 'archaeological child', 2),
  (5, PARSEDATETIME('2017-12-26 20:44:00', 'YYYY-MM-DD hh:mm:ss'), 'difficult dollar', 4),
  (6, PARSEDATETIME('2017-12-26 20:55:00', 'YYYY-MM-DD hh:mm:ss'), 'hard graphics', 3),
  (7, PARSEDATETIME('2017-12-26 21:06:00', 'YYYY-MM-DD hh:mm:ss'), 'minor lump', 7),
  (8, PARSEDATETIME('2017-12-26 21:17:00', 'YYYY-MM-DD hh:mm:ss'), 'rational physics', 4),
  (9, PARSEDATETIME('2017-12-26 21:28:00', 'YYYY-MM-DD hh:mm:ss'), 'tense sacrifice', 4),
  (10, PARSEDATETIME('2017-12-26 21:39:00', 'YYYY-MM-DD hh:mm:ss'), 'ashamed text', 4),
  (11, PARSEDATETIME('2017-12-26 21:50:00', 'YYYY-MM-DD hh:mm:ss'), 'dirty ambiguity', 2),
  (12, PARSEDATETIME('2017-12-26 22:01:00', 'YYYY-MM-DD hh:mm:ss'), 'heavy chip', 7),
  (13, PARSEDATETIME('2017-12-26 22:12:00', 'YYYY-MM-DD hh:mm:ss'), 'modest donor', 4)
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
  (4, 'READ_MESSAGE'),
  (4, 'CREATE_MESSAGE'),
  (4, 'DELETE_MESSAGE'),
  (4, 'READ_SELF_PROFILE'),
  (4, 'UPDATE_SELF_PROFILE'),
  (4, 'DELETE_SELF_PROFILE'),
  (5, 'READ_MESSAGE'),
  (5, 'CREATE_MESSAGE'),
  (5, 'DELETE_MESSAGE'),
  (5, 'READ_SELF_PROFILE'),
  (5, 'UPDATE_SELF_PROFILE'),
  (5, 'DELETE_SELF_PROFILE'),
  (5, 'READ_OTHER_PROFILE'),
  (5, 'UPDATE_OTHER_PROFILE'),
  (5, 'DELETE_OTHER_PROFILE'),
  (6, 'READ_MESSAGE'),
  (6, 'CREATE_MESSAGE'),
  (6, 'DELETE_MESSAGE'),
  (6, 'READ_SELF_PROFILE'),
  (6, 'UPDATE_SELF_PROFILE'),
  (6, 'DELETE_SELF_PROFILE'),
  (7, 'READ_MESSAGE'),
  (7, 'READ_SELF_PROFILE'),
  (8, 'READ_MESSAGE'),
  (8, 'CREATE_MESSAGE'),
  (8, 'DELETE_MESSAGE'),
  (8, 'READ_SELF_PROFILE'),
  (8, 'UPDATE_SELF_PROFILE'),
  (8, 'DELETE_SELF_PROFILE')
;

INSERT INTO applications(client_id, client_secret, name, user_id, redirect_uri) VALUES
  ('5c482cdb1dafea9b76701c882bd7336a7aac171a', '1cf413579715311ff293b9d62a25b309cfc581fddcf5a6db5de67a536af45e05', 'user-1 app', 1, 'http://localhost:8080/app/1'),
  ('f1bf374b27b5db582cf3aecd268858063990e698', '91933c6b059c544ed52f79073890d10b795161783072bd58f88678c2587a4e9f', 'user-5 app', 5, 'http://localhost:8080/app/5')
;

INSERT INTO authorization_codes(id, code, created, client_id, user_id) VALUES
  (1, '438c2afdbf37f3a50babcdb5d14f2c23744c437b', PARSEDATETIME('2018-01-11 20:00:00', 'YYYY-MM-DD hh:mm:ss'), '5c482cdb1dafea9b76701c882bd7336a7aac171a', 1),
  (2, 'b97e7b03aaa244031de3a893c76854b76c231485', PARSEDATETIME('2018-01-11 20:00:00', 'YYYY-MM-DD hh:mm:ss'), '5c482cdb1dafea9b76701c882bd7336a7aac171a', 3),
  (3, 'bcf269fa2b975e95f779d4d124f11341f546480d', PARSEDATETIME('2018-01-11 20:00:00', 'YYYY-MM-DD hh:mm:ss'), 'f1bf374b27b5db582cf3aecd268858063990e698', 5),
  (4, 'ef4f94efbf3375308f107c3c5c02d7c8fe686955', PARSEDATETIME('2018-01-11 20:00:00', 'YYYY-MM-DD hh:mm:ss'), 'f1bf374b27b5db582cf3aecd268858063990e698', 7)
;

INSERT INTO authorized_scopes(auth_code_id, scope) VALUES
  (1, 'READ_MESSAGE'),
  (1, 'WRITE_MESSAGE'),
  (1, 'READ_PROFILE'),
  (1, 'WRITE_PROFILE'),
  (2, 'READ_MESSAGE'),
  (2, 'WRITE_MESSAGE'),
  (2, 'READ_PROFILE'),
  (2, 'WRITE_PROFILE'),
  (3, 'READ_MESSAGE'),
  (3, 'READ_PROFILE'),
  (4, 'READ_MESSAGE'),
  (4, 'READ_PROFILE')
;

INSERT INTO tokens(id, created, access_token, refresh_token, auth_code_id) VALUES
  (1, PARSEDATETIME('2018-01-11 22:00:00', 'YYYY-MM-DD hh:mm:ss'), 'ba2f94a0241b1cbf4b8501811bcabf72301ae20af52f31d5ab38ee19efbd5ed7c48d48e8c34090e2a1ae6b22c8a148dbc9fb099f41722c96cd8766a0ee801b11', 'cbaeb1a3323ffe05a14d40da1bb89c175f13d674', 1),
  (2, PARSEDATETIME('2018-01-11 22:00:00', 'YYYY-MM-DD hh:mm:ss'), 'e988cbb84f2da19317c3de60d0bd6ca2ea1b1af2b3ff0ec42b7e116f95868f4dc0efc2ffba0cb1878ff5cc5428c5efc26d2b39ca512668b101094db38cec196e', '0f72cd7dd0b165326caf5497288f93a713fd779e', 2),
  (3, PARSEDATETIME('2018-01-11 22:00:00', 'YYYY-MM-DD hh:mm:ss'), 'd4e5bd476606d2e67901d9a0b515d3e5ca9fcb31f63091bbc4538d9b5dfc41484d26cf7dcf4c299554ff1868a4902a3b59f5267c1e716ae4dcd3dcbd2d503687', 'aceb16a2cf6c56b5b64ee7baa5d7dbea324bb622', 3),
  (4, PARSEDATETIME('2018-01-11 22:00:00', 'YYYY-MM-DD hh:mm:ss'), '98766e08730096538e80a04ad792c4e623a74f569ce9388209dc21eb94d109bd7001fbf0eda1b8c31199cf4a09e3dbbf600ba9f9b164019ba464a239e994452a', 'e343da24ddeece62ddc416400255d1c6525525fa', 4)
;

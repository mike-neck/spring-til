/*
 * Copyright 2018 Shinya Mochida
 *
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example;

public class User {

  private final Long id;
  private final String name;

  private User(final Long id, final String name) {
    this.id = id;
    this.name = name;
  }

  public static User of(final long id, final String username) {
    return new User(id, username);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (!(o instanceof User)) return false;

    final User user = (User) o;

    if (!id.equals(user.id)) return false;
    return name.equals(user.name);
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + name.hashCode();
    return result;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public static class Line {
      private String username;

      public String getUsername() {
          return username;
      }

      public void setUsername(final String username) {
          this.username = username;
      }

      public Line(final String username) {
          this.username = username;
      }

      public Line() {
      }

      public User toUserWithId(final long id) {
      return User.of(id, this.username);
      }
  }
}

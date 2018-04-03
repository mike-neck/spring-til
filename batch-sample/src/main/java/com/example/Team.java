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

public class Team {

  private final Long id;
  private final String identifier;
  private final String name;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Team{");
        sb.append("id=").append(id);
        sb.append(", identifier=").append(identifier);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (!(o instanceof Team)) return false;

    final Team team = (Team) o;

    if (!id.equals(team.id)) return false;
    return name.equals(team.name);
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

  public String getIdentifier() {
    return identifier;
  }

  public String getName() {
    return name;
  }

  private Team(final Long id, final String identifier, final String name) {
    this.id = id;
    this.identifier = identifier;
    this.name = name;
  }

  public static Team of(final long id, final String identifier, final String name) {
    return new Team(id, identifier, name);
  }

  public static class Line {
    private String identifier;
    private String name;

    public Line() {}

    public Line(final String identifier, final String name) {
      this.identifier = identifier;
      this.name = name;
    }

    public Team tomWithId(final long id) {
      return new Team(id, identifier, name);
    }

    public String getIdentifier() {
      return identifier;
    }

    public void setIdentifier(final String identifier) {
      this.identifier = identifier;
    }

    public String getName() {
      return name;
    }

    public void setName(final String name) {
      this.name = name;
    }
  }
}

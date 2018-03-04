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

import com.example.model.Role;
import com.example.model.TalkRoom;
import com.example.model.TalkRoomMember;
import com.example.model.UserAccount;
import com.example.talk.TalkRoomRepository;
import com.example.user.UserAccountRepository;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.map.primitive.MutableLongObjectMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.primitive.IntLists;
import org.eclipse.collections.impl.map.mutable.primitive.LongObjectHashMap;
import org.eclipse.collections.impl.tuple.Tuples;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

import static org.eclipse.collections.impl.tuple.primitive.PrimitiveTuples.pair;

@Configuration
public class InitialModelConfig {

    private final IdGenerator idGenerator;
    private final MutableMap<String, UserAccount> userAccounts;
    private final MutableLongObjectMap<TalkRoom> talkRooms = LongObjectHashMap.newMap();
    private final MutableList<TalkRoomMember> talkRoomMembers;

    @SuppressWarnings("unchecked")
    public InitialModelConfig(final IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
        final MutableList<UserAccount> users = Lists.mutable.of(
                UserAccount.of(idGenerator.generateLong(), "foo", "foo-pass", "FOO", Role.USER),
                UserAccount.of(idGenerator.generateLong(), "bar", "bar-pass", "BAR[ADMIN]", Role.USER, Role.ADMIN, Role.BETA_USER),
                UserAccount.of(idGenerator.generateLong(), "baz", "baz-pass", "BAZ[beta]", Role.USER, Role.BETA_USER),
                UserAccount.of(idGenerator.generateLong(), "qux", "qux-pass", "qux-pass", Role.USER)
        );
        userAccounts = users.groupByUniqueKey(UserAccount::getLoginId);
        final MutableList<TalkRoom> talkRoomList = Lists.mutable.of(
                TalkRoom.of(idGenerator.generateLong(), "all users", "talk room which contains all users."),
                TalkRoom.of(idGenerator.generateLong(), "admins", "admins"),
                TalkRoom.of(idGenerator.generateLong(), "beta users", "beta users")
        );
        talkRoomList.forEach(talkRoom -> talkRooms.put(talkRoom.getId(), talkRoom));
        final LocalDateTime now = LocalDateTime.now();
        talkRoomMembers = Lists.mutable.of(
                pair(0, IntLists.mutable.of(0, 1, 2, 3)),
                pair(1, IntLists.mutable.of(1)),
                pair(2, IntLists.mutable.of(1, 2))
        ).collect(p -> Tuples.pair(talkRoomList.get(p.getOne()), p.getTwo().collect(users::get)))
                .flatCollect(p -> p.getTwo().collect(ua -> new TalkRoomMember(p.getOne().getId(), ua.getId(), now)));
    }

    @Bean
    UserAccountRepository userAccountRepository() {
        return new UserAccountRepository(idGenerator, userAccounts);
    }

    @Bean
    TalkRoomRepository talkRoomRepository() {
        return new TalkRoomRepository(idGenerator, talkRooms, talkRoomMembers);
    }
}

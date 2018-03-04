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
package com.example.talk;

import com.example.IdGenerator;
import com.example.model.TalkRoom;
import com.example.model.TalkRoomMember;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.primitive.MutableLongObjectMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class TalkRoomRepository {

    private final IdGenerator idGenerator;
    private final MutableLongObjectMap<TalkRoom> talkRooms;
    private final MutableList<TalkRoomMember> talkRoomMembers;

    public TalkRoomRepository(final IdGenerator idGenerator,
            final MutableLongObjectMap<TalkRoom> talkRooms,
            final MutableList<TalkRoomMember> talkRoomMembers) {
        this.idGenerator = idGenerator;
        this.talkRooms = talkRooms;
        this.talkRoomMembers = talkRoomMembers;
    }

    public Flux<TalkRoom> findTalkRoomByUserId(final long userId) {
        return Flux.fromIterable(talkRoomMembers)
                .filter(member -> member.getUserId() == userId)
                .map(TalkRoomMember::getTalkRoomId)
                .map(talkRooms::get);
    }

    public Mono<TalkRoom> getTalkRoomByTalkRoomId(final long talkRoomId) {
        return Mono.justOrEmpty(talkRooms.get(talkRoomId));
    }
}

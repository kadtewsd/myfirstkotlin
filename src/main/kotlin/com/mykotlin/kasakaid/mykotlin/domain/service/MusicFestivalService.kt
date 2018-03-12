package com.mykotlin.kasakaid.mykotlin.domain.service

import com.mykotlin.kasakaid.mykotlin.domain.festival.Artist
import com.mykotlin.kasakaid.mykotlin.domain.festival.Member
import com.mykotlin.kasakaid.mykotlin.domain.festival.MusicFestival
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MusicFestivalService {

    fun getMusicFestival(id: Int): MusicFestival {

        val members:MutableList<Member> = mutableListOf()
        members.add(Member("山口一郎", "Guitar & Vocal"))
        members.add(Member("岩寺 基晴", "Guitar"))
        members.add(Member("草刈 愛美", "Bass & Chorus"))
        members.add(Member("江島 啓一", "Drums"))

        val artists:MutableList<Artist> = mutableListOf()
        artists.add(Artist(1, members))

        return MusicFestival(1, "Rockin' on Japan", "茨城県ひたち市", LocalDateTime.of(2017, 8,11, 9, 0, 0), artists)
    }
}

package com.mykotlin.kasakaid.mykotlin.domain.service


import com.mykotlin.kasakaid.mykotlin.AbstractBaseTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class MusicFestivalServiceTest() : AbstractBaseTest() {

    @Autowired
    lateinit var musicFestivalService: MusicFestivalService

    @Test
    fun 最初のKotlinテスト() {
        val musicFestival = musicFestivalService.getMusicFestival(1)
        assertThat(musicFestival.name).isEqualTo("Rockin' on Japan")
    }
}
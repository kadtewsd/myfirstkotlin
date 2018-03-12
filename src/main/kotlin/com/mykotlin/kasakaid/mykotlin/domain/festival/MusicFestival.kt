package com.mykotlin.kasakaid.mykotlin.domain.festival

import java.time.LocalDateTime

class MusicFestival(val id:Int, val name:String, val place:String, val startDate: LocalDateTime, val artists:List<Artist> ) {

}

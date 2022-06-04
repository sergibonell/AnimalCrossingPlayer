package cat.itb.totaplayer

import cat.itb.totaplayer.api.Name

object SongUtilities {
    fun getNameFromLang(language: Int, names: Name): String{
        var name = "ERROR"
        when(language) {
            0 -> name = names.nameUSen
            1 -> name = names.nameEUen
            2 -> name = names.nameEUde
            3 -> name = names.nameEUes
            4 -> name = names.nameUSes
            5 -> name = names.nameEUfr
            6 -> name = names.nameUSfr
            7 -> name = names.nameEUit
            8 -> name = names.nameEUnl
            9 -> name = names.nameCNzh
            10 -> name = names.nameTWzh
            11 -> name = names.nameJPja
            12 -> name = names.nameKRko
            13 -> name = names.nameEUru
        }
        return name
    }
}
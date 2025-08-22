package com.wojdor.memolki.data.source.card.local

import com.wojdor.memolki.R
import com.wojdor.memolki.data.entity.CardEntity
import com.wojdor.memolki.data.entity.CardPairEntity
import javax.inject.Inject

class AllCardPairsLocalDataSource @Inject constructor() : AllCardPairsDataSource {

    override fun getAllCardPairs(): List<CardPairEntity> = listOf(
        CardPairEntity(
            id = "banana",
            pair = Pair(
                CardEntity.Image(
                    id = "banana_whole",
                    textRes = R.string.banana,
                    imageRes = R.drawable.img_banana_whole
                ),
                CardEntity.Image(
                    id = "banana_half",
                    textRes = R.string.banana,
                    imageRes = R.drawable.img_banana_half
                )
            )
        ),
        CardPairEntity(
            id = "apple",
            pair = Pair(
                CardEntity.Image(
                    id = "apple_whole",
                    textRes = R.string.apple,
                    imageRes = R.drawable.img_apple_whole
                ),
                CardEntity.Image(
                    id = "apple_half",
                    textRes = R.string.apple,
                    imageRes = R.drawable.img_apple_half
                )
            )
        ),
        CardPairEntity(
            id = "strawberry",
            pair = Pair(
                CardEntity.Image(
                    id = "strawberry_whole",
                    textRes = R.string.strawberry,
                    imageRes = R.drawable.img_strawberry_whole
                ),
                CardEntity.Image(
                    id = "strawberry_half",
                    textRes = R.string.strawberry,
                    imageRes = R.drawable.img_strawberry_half
                )
            )
        ),
        CardPairEntity(
            id = "orange",
            pair = Pair(
                CardEntity.Image(
                    id = "orange_whole",
                    textRes = R.string.orange,
                    imageRes = R.drawable.img_orange_whole
                ),
                CardEntity.Image(
                    id = "orange_half",
                    textRes = R.string.orange,
                    imageRes = R.drawable.img_orange_half
                )
            )
        ),
        CardPairEntity(
            id = "grape",
            pair = Pair(
                CardEntity.Image(
                    id = "grape_whole",
                    textRes = R.string.grape,
                    imageRes = R.drawable.img_grape_whole
                ),
                CardEntity.Image(
                    id = "grape_half",
                    textRes = R.string.grape,
                    imageRes = R.drawable.img_grape_half
                )
            )
        ),
        CardPairEntity(
            id = "watermelon",
            pair = Pair(
                CardEntity.Image(
                    id = "watermelon_whole",
                    textRes = R.string.watermelon,
                    imageRes = R.drawable.img_watermelon_whole
                ),
                CardEntity.Image(
                    id = "watermelon_half",
                    textRes = R.string.watermelon,
                    imageRes = R.drawable.img_watermelon_half
                )
            )
        ),
        CardPairEntity(
            id = "mango",
            pair = Pair(
                CardEntity.Image(
                    id = "mango_whole",
                    textRes = R.string.mango,
                    imageRes = R.drawable.img_mango_whole
                ),
                CardEntity.Image(
                    id = "mango_half",
                    textRes = R.string.mango,
                    imageRes = R.drawable.img_mango_half
                )
            )
        ),
        CardPairEntity(
            id = "peach",
            pair = Pair(
                CardEntity.Image(
                    id = "peach_whole",
                    textRes = R.string.peach,
                    imageRes = R.drawable.img_peach_whole
                ),
                CardEntity.Image(
                    id = "peach_half",
                    textRes = R.string.peach,
                    imageRes = R.drawable.img_peach_half
                )
            )
        ),
        CardPairEntity(
            id = "pineapple",
            pair = Pair(
                CardEntity.Image(
                    id = "pineapple_whole",
                    textRes = R.string.pineapple,
                    imageRes = R.drawable.img_pineapple_whole
                ),
                CardEntity.Image(
                    id = "pineapple_half",
                    textRes = R.string.pineapple,
                    imageRes = R.drawable.img_pineapple_half
                )
            )
        ),
        CardPairEntity(
            id = "blueberry",
            pair = Pair(
                CardEntity.Image(
                    id = "blueberry_whole",
                    textRes = R.string.blueberry,
                    imageRes = R.drawable.img_blueberry_whole
                ),
                CardEntity.Image(
                    id = "blueberry_half",
                    textRes = R.string.blueberry,
                    imageRes = R.drawable.img_blueberry_half
                )
            )
        ),
        CardPairEntity(
            id = "lemon",
            pair = Pair(
                CardEntity.Image(
                    id = "lemon_whole",
                    textRes = R.string.lemon,
                    imageRes = R.drawable.img_lemon_whole
                ),
                CardEntity.Image(
                    id = "lemon_half",
                    textRes = R.string.lemon,
                    imageRes = R.drawable.img_lemon_half
                )
            )
        ),
        CardPairEntity(
            id = "raspberry",
            pair = Pair(
                CardEntity.Image(
                    id = "raspberry_whole",
                    textRes = R.string.raspberry,
                    imageRes = R.drawable.img_raspberry_whole
                ),
                CardEntity.Image(
                    id = "raspberry_half",
                    textRes = R.string.raspberry,
                    imageRes = R.drawable.img_raspberry_half
                )
            )
        ),
        CardPairEntity(
            id = "cherry",
            pair = Pair(
                CardEntity.Image(
                    id = "cherry_whole",
                    textRes = R.string.cherry,
                    imageRes = R.drawable.img_cherry_whole
                ),
                CardEntity.Image(
                    id = "cherry_half",
                    textRes = R.string.cherry,
                    imageRes = R.drawable.img_cherry_half
                )
            )
        ),
        CardPairEntity(
            id = "pear",
            pair = Pair(
                CardEntity.Image(
                    id = "pear_whole",
                    textRes = R.string.pear,
                    imageRes = R.drawable.img_pear_whole
                ),
                CardEntity.Image(
                    id = "pear_half",
                    textRes = R.string.pear,
                    imageRes = R.drawable.img_pear_half
                )
            )
        ),
        CardPairEntity(
            id = "avocado",
            pair = Pair(
                CardEntity.Image(
                    id = "avocado_whole",
                    textRes = R.string.avocado,
                    imageRes = R.drawable.img_avocado_whole
                ),
                CardEntity.Image(
                    id = "avocado_half",
                    textRes = R.string.avocado,
                    imageRes = R.drawable.img_avocado_half
                )
            )
        ),
        CardPairEntity(
            id = "kiwi",
            pair = Pair(
                CardEntity.Image(
                    id = "kiwi_whole",
                    textRes = R.string.kiwi,
                    imageRes = R.drawable.img_kiwi_whole
                ),
                CardEntity.Image(
                    id = "kiwi_half",
                    textRes = R.string.kiwi,
                    imageRes = R.drawable.img_kiwi_half
                )
            )
        ),
        CardPairEntity(
            id = "lime",
            pair = Pair(
                CardEntity.Image(
                    id = "lime_whole",
                    textRes = R.string.lime,
                    imageRes = R.drawable.img_lime_whole
                ),
                CardEntity.Image(
                    id = "lime_half",
                    textRes = R.string.lime,
                    imageRes = R.drawable.img_lime_half
                )
            )
        ),
        CardPairEntity(
            id = "plum",
            pair = Pair(
                CardEntity.Image(
                    id = "plum_whole",
                    textRes = R.string.plum,
                    imageRes = R.drawable.img_plum_whole
                ),
                CardEntity.Image(
                    id = "plum_half",
                    textRes = R.string.plum,
                    imageRes = R.drawable.img_plum_half
                )
            )
        ),
        CardPairEntity(
            id = "cantaloupe",
            pair = Pair(
                CardEntity.Image(
                    id = "cantaloupe_whole",
                    textRes = R.string.cantaloupe,
                    imageRes = R.drawable.img_cantaloupe_whole
                ),
                CardEntity.Image(
                    id = "cantaloupe_half",
                    textRes = R.string.cantaloupe,
                    imageRes = R.drawable.img_cantaloupe_half
                )
            )
        ),
        CardPairEntity(
            id = "coconut",
            pair = Pair(
                CardEntity.Image(
                    id = "coconut_whole",
                    textRes = R.string.coconut,
                    imageRes = R.drawable.img_coconut_whole
                ),
                CardEntity.Image(
                    id = "coconut_half",
                    textRes = R.string.coconut,
                    imageRes = R.drawable.img_coconut_half
                )
            )
        ),
        CardPairEntity(
            id = "pomegranate",
            pair = Pair(
                CardEntity.Image(
                    id = "pomegranate_whole",
                    textRes = R.string.pomegranate,
                    imageRes = R.drawable.img_pomegranate_whole
                ),
                CardEntity.Image(
                    id = "pomegranate_half",
                    textRes = R.string.pomegranate,
                    imageRes = R.drawable.img_pomegranate_half
                )
            )
        ),
        CardPairEntity(
            id = "apricot",
            pair = Pair(
                CardEntity.Image(
                    id = "apricot_whole",
                    textRes = R.string.apricot,
                    imageRes = R.drawable.img_apricot_whole
                ),
                CardEntity.Image(
                    id = "apricot_half",
                    textRes = R.string.apricot,
                    imageRes = R.drawable.img_apricot_half
                )
            )
        ),
        CardPairEntity(
            id = "nectarine",
            pair = Pair(
                CardEntity.Image(
                    id = "nectarine_whole",
                    textRes = R.string.nectarine,
                    imageRes = R.drawable.img_nectarine_whole
                ),
                CardEntity.Image(
                    id = "nectarine_half",
                    textRes = R.string.nectarine,
                    imageRes = R.drawable.img_nectarine_half
                )
            )
        ),
        CardPairEntity(
            id = "grapefruit",
            pair = Pair(
                CardEntity.Image(
                    id = "grapefruit_whole",
                    textRes = R.string.grapefruit,
                    imageRes = R.drawable.img_grapefruit_whole
                ),
                CardEntity.Image(
                    id = "grapefruit_half",
                    textRes = R.string.grapefruit,
                    imageRes = R.drawable.img_grapefruit_half
                )
            )
        ),
        CardPairEntity(
            id = "blackberry",
            pair = Pair(
                CardEntity.Image(
                    id = "blackberry_whole",
                    textRes = R.string.blackberry,
                    imageRes = R.drawable.img_blackberry_whole
                ),
                CardEntity.Image(
                    id = "blackberry_half",
                    textRes = R.string.blackberry,
                    imageRes = R.drawable.img_blackberry_half
                )
            )
        ),
        CardPairEntity(
            id = "fig",
            pair = Pair(
                CardEntity.Image(
                    id = "fig_whole",
                    textRes = R.string.fig,
                    imageRes = R.drawable.img_fig_whole
                ),
                CardEntity.Image(
                    id = "fig_half",
                    textRes = R.string.fig,
                    imageRes = R.drawable.img_fig_half
                )
            )
        ),
        CardPairEntity(
            id = "papaya",
            pair = Pair(
                CardEntity.Image(
                    id = "papaya_whole",
                    textRes = R.string.papaya,
                    imageRes = R.drawable.img_papaya_whole
                ),
                CardEntity.Image(
                    id = "papaya_half",
                    textRes = R.string.papaya,
                    imageRes = R.drawable.img_papaya_half
                )
            )
        ),
        CardPairEntity(
            id = "cranberry",
            pair = Pair(
                CardEntity.Image(
                    id = "cranberry_whole",
                    textRes = R.string.cranberry,
                    imageRes = R.drawable.img_cranberry_whole
                ),
                CardEntity.Image(
                    id = "cranberry_half",
                    textRes = R.string.cranberry,
                    imageRes = R.drawable.img_cranberry_half
                )
            )
        ),
        CardPairEntity(
            id = "guava",
            pair = Pair(
                CardEntity.Image(
                    id = "guava_whole",
                    textRes = R.string.guava,
                    imageRes = R.drawable.img_guava_whole
                ),
                CardEntity.Image(
                    id = "guava_half",
                    textRes = R.string.guava,
                    imageRes = R.drawable.img_guava_half
                )
            )
        ),
        CardPairEntity(
            id = "tangerine",
            pair = Pair(
                CardEntity.Image(
                    id = "tangerine_whole",
                    textRes = R.string.tangerine,
                    imageRes = R.drawable.img_tangerine_whole
                ),
                CardEntity.Image(
                    id = "tangerine_half",
                    textRes = R.string.tangerine,
                    imageRes = R.drawable.img_tangerine_half
                )
            )
        ),
        CardPairEntity(
            id = "honeydew_melon",
            pair = Pair(
                CardEntity.Image(
                    id = "honeydew_melon_whole",
                    textRes = R.string.honeydew_melon,
                    imageRes = R.drawable.img_honeydew_melon_whole
                ),
                CardEntity.Image(
                    id = "honeydew_melon_half",
                    textRes = R.string.honeydew_melon,
                    imageRes = R.drawable.img_honeydew_melon_half
                )
            )
        ),
        CardPairEntity(
            id = "passion_fruit",
            pair = Pair(
                CardEntity.Image(
                    id = "passion_fruit_whole",
                    textRes = R.string.passion_fruit,
                    imageRes = R.drawable.img_passion_fruit_whole
                ),
                CardEntity.Image(
                    id = "passion_fruit_half",
                    textRes = R.string.passion_fruit,
                    imageRes = R.drawable.img_passion_fruit_half
                )
            )
        ),
        CardPairEntity(
            id = "lychee",
            pair = Pair(
                CardEntity.Image(
                    id = "lychee_whole",
                    textRes = R.string.lychee,
                    imageRes = R.drawable.img_lychee_whole
                ),
                CardEntity.Image(
                    id = "lychee_half",
                    textRes = R.string.lychee,
                    imageRes = R.drawable.img_lychee_half
                )
            )
        ),
        CardPairEntity(
            id = "dragon_fruit",
            pair = Pair(
                CardEntity.Image(
                    id = "dragon_fruit_whole",
                    textRes = R.string.dragon_fruit,
                    imageRes = R.drawable.img_dragon_fruit_whole
                ),
                CardEntity.Image(
                    id = "dragon_fruit_half",
                    textRes = R.string.dragon_fruit,
                    imageRes = R.drawable.img_dragon_fruit_half
                )
            )
        ),
        CardPairEntity(
            id = "date",
            pair = Pair(
                CardEntity.Image(
                    id = "date_whole",
                    textRes = R.string.date,
                    imageRes = R.drawable.img_date_whole
                ),
                CardEntity.Image(
                    id = "date_half",
                    textRes = R.string.date,
                    imageRes = R.drawable.img_date_half
                )
            )
        ),
        CardPairEntity(
            id = "persimmon",
            pair = Pair(
                CardEntity.Image(
                    id = "persimmon_whole",
                    textRes = R.string.persimmon,
                    imageRes = R.drawable.img_persimmon_whole
                ),
                CardEntity.Image(
                    id = "persimmon_half",
                    textRes = R.string.persimmon,
                    imageRes = R.drawable.img_persimmon_half
                )
            )
        ),
        CardPairEntity(
            id = "jackfruit",
            pair = Pair(
                CardEntity.Image(
                    id = "jackfruit_whole",
                    textRes = R.string.jackfruit,
                    imageRes = R.drawable.img_jackfruit_whole
                ),
                CardEntity.Image(
                    id = "jackfruit_half",
                    textRes = R.string.jackfruit,
                    imageRes = R.drawable.img_jackfruit_half
                )
            )
        ),
        CardPairEntity(
            id = "star_fruit",
            pair = Pair(
                CardEntity.Image(
                    id = "star_fruit_whole",
                    textRes = R.string.star_fruit,
                    imageRes = R.drawable.img_star_fruit_whole
                ),
                CardEntity.Image(
                    id = "star_fruit_half",
                    textRes = R.string.star_fruit,
                    imageRes = R.drawable.img_star_fruit_half
                )
            )
        ),
        CardPairEntity(
            id = "mangosteen",
            pair = Pair(
                CardEntity.Image(
                    id = "mangosteen_whole",
                    textRes = R.string.mangosteen,
                    imageRes = R.drawable.img_mangosteen_whole
                ),
                CardEntity.Image(
                    id = "mangosteen_half",
                    textRes = R.string.mangosteen,
                    imageRes = R.drawable.img_mangosteen_half
                )
            )
        ),
        CardPairEntity(
            id = "citron",
            pair = Pair(
                CardEntity.Image(
                    id = "citron_whole",
                    textRes = R.string.citron,
                    imageRes = R.drawable.img_citron_whole
                ),
                CardEntity.Image(
                    id = "citron_half",
                    textRes = R.string.citron,
                    imageRes = R.drawable.img_citron_half
                )
            )
        ),
        CardPairEntity(
            id = "durian",
            pair = Pair(
                CardEntity.Image(
                    id = "durian_whole",
                    textRes = R.string.durian,
                    imageRes = R.drawable.img_durian_whole
                ),
                CardEntity.Image(
                    id = "durian_half",
                    textRes = R.string.durian,
                    imageRes = R.drawable.img_durian_half
                )
            )
        ),
        CardPairEntity(
            id = "kumquat",
            pair = Pair(
                CardEntity.Image(
                    id = "kumquat_whole",
                    textRes = R.string.kumquat,
                    imageRes = R.drawable.img_kumquat_whole
                ),
                CardEntity.Image(
                    id = "kumquat_half",
                    textRes = R.string.kumquat,
                    imageRes = R.drawable.img_kumquat_half
                )
            )
        ),
        CardPairEntity(
            id = "blackcurrant",
            pair = Pair(
                CardEntity.Image(
                    id = "blackcurrant_whole",
                    textRes = R.string.blackcurrant,
                    imageRes = R.drawable.img_blackcurrant_whole
                ),
                CardEntity.Image(
                    id = "blackcurrant_half",
                    textRes = R.string.blackcurrant,
                    imageRes = R.drawable.img_blackcurrant_half
                )
            )
        ),
        CardPairEntity(
            id = "gooseberry",
            pair = Pair(
                CardEntity.Image(
                    id = "gooseberry_whole",
                    textRes = R.string.gooseberry,
                    imageRes = R.drawable.img_gooseberry_whole
                ),
                CardEntity.Image(
                    id = "gooseberry_half",
                    textRes = R.string.gooseberry,
                    imageRes = R.drawable.img_gooseberry_half
                )
            )
        ),
        CardPairEntity(
            id = "rambutan",
            pair = Pair(
                CardEntity.Image(
                    id = "rambutan_whole",
                    textRes = R.string.rambutan,
                    imageRes = R.drawable.img_rambutan_whole
                ),
                CardEntity.Image(
                    id = "rambutan_half",
                    textRes = R.string.rambutan,
                    imageRes = R.drawable.img_rambutan_half
                )
            )
        ),
        CardPairEntity(
            id = "mulberry",
            pair = Pair(
                CardEntity.Image(
                    id = "mulberry_whole",
                    textRes = R.string.mulberry,
                    imageRes = R.drawable.img_mulberry_whole
                ),
                CardEntity.Image(
                    id = "mulberry_half",
                    textRes = R.string.mulberry,
                    imageRes = R.drawable.img_mulberry_half
                )
            )
        ),
        CardPairEntity(
            id = "elderberry",
            pair = Pair(
                CardEntity.Image(
                    id = "elderberry_whole",
                    textRes = R.string.elderberry,
                    imageRes = R.drawable.img_elderberry_whole
                ),
                CardEntity.Image(
                    id = "elderberry_half",
                    textRes = R.string.elderberry,
                    imageRes = R.drawable.img_elderberry_half
                )
            )
        ),
        CardPairEntity(
            id = "açaí",
            pair = Pair(
                CardEntity.Image(
                    id = "açaí_whole",
                    textRes = R.string.acai,
                    imageRes = R.drawable.img_acai_whole
                ),
                CardEntity.Image(
                    id = "açaí_half",
                    textRes = R.string.acai,
                    imageRes = R.drawable.img_acai_half
                )
            )
        ),
        CardPairEntity(
            id = "quince",
            pair = Pair(
                CardEntity.Image(
                    id = "quince_whole",
                    textRes = R.string.quince,
                    imageRes = R.drawable.img_quince_whole
                ),
                CardEntity.Image(
                    id = "quince_half",
                    textRes = R.string.quince,
                    imageRes = R.drawable.img_quince_half
                )
            )
        ),
        CardPairEntity(
            id = "redcurrant",
            pair = Pair(
                CardEntity.Image(
                    id = "redcurrant_whole",
                    textRes = R.string.redcurrant,
                    imageRes = R.drawable.img_redcurrant_whole
                ),
                CardEntity.Image(
                    id = "redcurrant_half",
                    textRes = R.string.redcurrant,
                    imageRes = R.drawable.img_redcurrant_half
                )
            )
        ),
        CardPairEntity(
            id = "soursop",
            pair = Pair(
                CardEntity.Image(
                    id = "soursop_whole",
                    textRes = R.string.soursop,
                    imageRes = R.drawable.img_soursop_whole
                ),
                CardEntity.Image(
                    id = "soursop_half",
                    textRes = R.string.soursop,
                    imageRes = R.drawable.img_soursop_half
                )
            )
        ),
        CardPairEntity(
            id = "kiwano",
            pair = Pair(
                CardEntity.Image(
                    id = "kiwano_whole",
                    textRes = R.string.kiwano,
                    imageRes = R.drawable.img_kiwano_whole
                ),
                CardEntity.Image(
                    id = "kiwano_half",
                    textRes = R.string.kiwano,
                    imageRes = R.drawable.img_kiwano_half
                )
            )
        ),
        CardPairEntity(
            id = "longan",
            pair = Pair(
                CardEntity.Image(
                    id = "longan_whole",
                    textRes = R.string.longan,
                    imageRes = R.drawable.img_longan_whole
                ),
                CardEntity.Image(
                    id = "longan_half",
                    textRes = R.string.longan,
                    imageRes = R.drawable.img_longan_half
                )
            )
        ),
        CardPairEntity(
            id = "tamarind",
            pair = Pair(
                CardEntity.Image(
                    id = "tamarind_whole",
                    textRes = R.string.tamarind,
                    imageRes = R.drawable.img_tamarind_whole
                ),
                CardEntity.Image(
                    id = "tamarind_half",
                    textRes = R.string.tamarind,
                    imageRes = R.drawable.img_tamarind_half
                )
            )
        ),
        CardPairEntity(
            id = "pomelo",
            pair = Pair(
                CardEntity.Image(
                    id = "pomelo_whole",
                    textRes = R.string.pomelo,
                    imageRes = R.drawable.img_pomelo_whole
                ),
                CardEntity.Image(
                    id = "pomelo_half",
                    textRes = R.string.pomelo,
                    imageRes = R.drawable.img_pomelo_half
                )
            )
        ),
        CardPairEntity(
            id = "jujube",
            pair = Pair(
                CardEntity.Image(
                    id = "jujube_whole",
                    textRes = R.string.jujube,
                    imageRes = R.drawable.img_jujube_whole
                ),
                CardEntity.Image(
                    id = "jujube_half",
                    textRes = R.string.jujube,
                    imageRes = R.drawable.img_jujube_half
                )
            )
        ),
        CardPairEntity(
            id = "cape_gooseberry",
            pair = Pair(
                CardEntity.Image(
                    id = "cape_gooseberry_whole",
                    textRes = R.string.cape_gooseberry,
                    imageRes = R.drawable.img_cape_gooseberry_whole
                ),
                CardEntity.Image(
                    id = "cape_gooseberry_half",
                    textRes = R.string.cape_gooseberry,
                    imageRes = R.drawable.img_cape_gooseberry_half
                )
            )
        ),
        CardPairEntity(
            id = "feijoa",
            pair = Pair(
                CardEntity.Image(
                    id = "feijoa_whole",
                    textRes = R.string.feijoa,
                    imageRes = R.drawable.img_feijoa_whole
                ),
                CardEntity.Image(
                    id = "feijoa_half",
                    textRes = R.string.feijoa,
                    imageRes = R.drawable.img_feijoa_half
                )
            )
        ),
        CardPairEntity(
            id = "salak",
            pair = Pair(
                CardEntity.Image(
                    id = "salak_whole",
                    textRes = R.string.salak,
                    imageRes = R.drawable.img_salak_whole
                ),
                CardEntity.Image(
                    id = "salak_half",
                    textRes = R.string.salak,
                    imageRes = R.drawable.img_salak_half
                )
            )
        ),
        CardPairEntity(
            id = "loquat",
            pair = Pair(
                CardEntity.Image(
                    id = "loquat_whole",
                    textRes = R.string.loquat,
                    imageRes = R.drawable.img_loquat_whole
                ),
                CardEntity.Image(
                    id = "loquat_half",
                    textRes = R.string.loquat,
                    imageRes = R.drawable.img_loquat_half
                )
            )
        )
    )
}

package com.wojdor.memolki.data.local.card

import com.wojdor.memolki.R
import com.wojdor.memolki.shared.resources.*
import com.wojdor.memolki.data.entity.CardEntity
import com.wojdor.memolki.data.entity.CardPairEntity
import com.wojdor.memolki.data.local.datastore.card.AllCardPairsDataSource
import javax.inject.Inject

class AllCardPairsLocalDataSource @Inject constructor() : AllCardPairsDataSource {

    override fun getAllCardPairs(): List<CardPairEntity> = listOf(
        CardPairEntity(
            id = "banana",
            pair = Pair(
                CardEntity.Image(
                    id = "banana_whole",
                    textRes = Res.string.banana,
                    imageRes = R.drawable.img_banana_whole
                ),
                CardEntity.Image(
                    id = "banana_half",
                    textRes = Res.string.banana,
                    imageRes = R.drawable.img_banana_half
                )
            )
        ),
        CardPairEntity(
            id = "apple",
            pair = Pair(
                CardEntity.Image(
                    id = "apple_whole",
                    textRes = Res.string.apple,
                    imageRes = R.drawable.img_apple_whole
                ),
                CardEntity.Image(
                    id = "apple_half",
                    textRes = Res.string.apple,
                    imageRes = R.drawable.img_apple_half
                )
            )
        ),
        CardPairEntity(
            id = "strawberry",
            pair = Pair(
                CardEntity.Image(
                    id = "strawberry_whole",
                    textRes = Res.string.strawberry,
                    imageRes = R.drawable.img_strawberry_whole
                ),
                CardEntity.Image(
                    id = "strawberry_half",
                    textRes = Res.string.strawberry,
                    imageRes = R.drawable.img_strawberry_half
                )
            )
        ),
        CardPairEntity(
            id = "orange",
            pair = Pair(
                CardEntity.Image(
                    id = "orange_whole",
                    textRes = Res.string.orange,
                    imageRes = R.drawable.img_orange_whole
                ),
                CardEntity.Image(
                    id = "orange_half",
                    textRes = Res.string.orange,
                    imageRes = R.drawable.img_orange_half
                )
            )
        ),
        CardPairEntity(
            id = "grape",
            pair = Pair(
                CardEntity.Image(
                    id = "grape_whole",
                    textRes = Res.string.grape,
                    imageRes = R.drawable.img_grape_whole
                ),
                CardEntity.Image(
                    id = "grape_half",
                    textRes = Res.string.grape,
                    imageRes = R.drawable.img_grape_half
                )
            )
        ),
        CardPairEntity(
            id = "watermelon",
            pair = Pair(
                CardEntity.Image(
                    id = "watermelon_whole",
                    textRes = Res.string.watermelon,
                    imageRes = R.drawable.img_watermelon_whole
                ),
                CardEntity.Image(
                    id = "watermelon_half",
                    textRes = Res.string.watermelon,
                    imageRes = R.drawable.img_watermelon_half
                )
            )
        ),
        CardPairEntity(
            id = "mango",
            pair = Pair(
                CardEntity.Image(
                    id = "mango_whole",
                    textRes = Res.string.mango,
                    imageRes = R.drawable.img_mango_whole
                ),
                CardEntity.Image(
                    id = "mango_half",
                    textRes = Res.string.mango,
                    imageRes = R.drawable.img_mango_half
                )
            )
        ),
        CardPairEntity(
            id = "peach",
            pair = Pair(
                CardEntity.Image(
                    id = "peach_whole",
                    textRes = Res.string.peach,
                    imageRes = R.drawable.img_peach_whole
                ),
                CardEntity.Image(
                    id = "peach_half",
                    textRes = Res.string.peach,
                    imageRes = R.drawable.img_peach_half
                )
            )
        ),
        CardPairEntity(
            id = "pineapple",
            pair = Pair(
                CardEntity.Image(
                    id = "pineapple_whole",
                    textRes = Res.string.pineapple,
                    imageRes = R.drawable.img_pineapple_whole
                ),
                CardEntity.Image(
                    id = "pineapple_half",
                    textRes = Res.string.pineapple,
                    imageRes = R.drawable.img_pineapple_half
                )
            )
        ),
        CardPairEntity(
            id = "blueberry",
            pair = Pair(
                CardEntity.Image(
                    id = "blueberry_whole",
                    textRes = Res.string.blueberry,
                    imageRes = R.drawable.img_blueberry_whole
                ),
                CardEntity.Image(
                    id = "blueberry_half",
                    textRes = Res.string.blueberry,
                    imageRes = R.drawable.img_blueberry_half
                )
            )
        ),
        CardPairEntity(
            id = "lemon",
            pair = Pair(
                CardEntity.Image(
                    id = "lemon_whole",
                    textRes = Res.string.lemon,
                    imageRes = R.drawable.img_lemon_whole
                ),
                CardEntity.Image(
                    id = "lemon_half",
                    textRes = Res.string.lemon,
                    imageRes = R.drawable.img_lemon_half
                )
            )
        ),
        CardPairEntity(
            id = "raspberry",
            pair = Pair(
                CardEntity.Image(
                    id = "raspberry_whole",
                    textRes = Res.string.raspberry,
                    imageRes = R.drawable.img_raspberry_whole
                ),
                CardEntity.Image(
                    id = "raspberry_half",
                    textRes = Res.string.raspberry,
                    imageRes = R.drawable.img_raspberry_half
                )
            )
        ),
        CardPairEntity(
            id = "cherry",
            pair = Pair(
                CardEntity.Image(
                    id = "cherry_whole",
                    textRes = Res.string.cherry,
                    imageRes = R.drawable.img_cherry_whole
                ),
                CardEntity.Image(
                    id = "cherry_half",
                    textRes = Res.string.cherry,
                    imageRes = R.drawable.img_cherry_half
                )
            )
        ),
        CardPairEntity(
            id = "pear",
            pair = Pair(
                CardEntity.Image(
                    id = "pear_whole",
                    textRes = Res.string.pear,
                    imageRes = R.drawable.img_pear_whole
                ),
                CardEntity.Image(
                    id = "pear_half",
                    textRes = Res.string.pear,
                    imageRes = R.drawable.img_pear_half
                )
            )
        ),
        CardPairEntity(
            id = "avocado",
            pair = Pair(
                CardEntity.Image(
                    id = "avocado_whole",
                    textRes = Res.string.avocado,
                    imageRes = R.drawable.img_avocado_whole
                ),
                CardEntity.Image(
                    id = "avocado_half",
                    textRes = Res.string.avocado,
                    imageRes = R.drawable.img_avocado_half
                )
            )
        ),
        CardPairEntity(
            id = "kiwi",
            pair = Pair(
                CardEntity.Image(
                    id = "kiwi_whole",
                    textRes = Res.string.kiwi,
                    imageRes = R.drawable.img_kiwi_whole
                ),
                CardEntity.Image(
                    id = "kiwi_half",
                    textRes = Res.string.kiwi,
                    imageRes = R.drawable.img_kiwi_half
                )
            )
        ),
        CardPairEntity(
            id = "lime",
            pair = Pair(
                CardEntity.Image(
                    id = "lime_whole",
                    textRes = Res.string.lime,
                    imageRes = R.drawable.img_lime_whole
                ),
                CardEntity.Image(
                    id = "lime_half",
                    textRes = Res.string.lime,
                    imageRes = R.drawable.img_lime_half
                )
            )
        ),
        CardPairEntity(
            id = "plum",
            pair = Pair(
                CardEntity.Image(
                    id = "plum_whole",
                    textRes = Res.string.plum,
                    imageRes = R.drawable.img_plum_whole
                ),
                CardEntity.Image(
                    id = "plum_half",
                    textRes = Res.string.plum,
                    imageRes = R.drawable.img_plum_half
                )
            )
        ),
        CardPairEntity(
            id = "cantaloupe",
            pair = Pair(
                CardEntity.Image(
                    id = "cantaloupe_whole",
                    textRes = Res.string.cantaloupe,
                    imageRes = R.drawable.img_cantaloupe_whole
                ),
                CardEntity.Image(
                    id = "cantaloupe_half",
                    textRes = Res.string.cantaloupe,
                    imageRes = R.drawable.img_cantaloupe_half
                )
            )
        ),
        CardPairEntity(
            id = "coconut",
            pair = Pair(
                CardEntity.Image(
                    id = "coconut_whole",
                    textRes = Res.string.coconut,
                    imageRes = R.drawable.img_coconut_whole
                ),
                CardEntity.Image(
                    id = "coconut_half",
                    textRes = Res.string.coconut,
                    imageRes = R.drawable.img_coconut_half
                )
            )
        ),
        CardPairEntity(
            id = "pomegranate",
            pair = Pair(
                CardEntity.Image(
                    id = "pomegranate_whole",
                    textRes = Res.string.pomegranate,
                    imageRes = R.drawable.img_pomegranate_whole
                ),
                CardEntity.Image(
                    id = "pomegranate_half",
                    textRes = Res.string.pomegranate,
                    imageRes = R.drawable.img_pomegranate_half
                )
            )
        ),
        CardPairEntity(
            id = "apricot",
            pair = Pair(
                CardEntity.Image(
                    id = "apricot_whole",
                    textRes = Res.string.apricot,
                    imageRes = R.drawable.img_apricot_whole
                ),
                CardEntity.Image(
                    id = "apricot_half",
                    textRes = Res.string.apricot,
                    imageRes = R.drawable.img_apricot_half
                )
            )
        ),
        CardPairEntity(
            id = "nectarine",
            pair = Pair(
                CardEntity.Image(
                    id = "nectarine_whole",
                    textRes = Res.string.nectarine,
                    imageRes = R.drawable.img_nectarine_whole
                ),
                CardEntity.Image(
                    id = "nectarine_half",
                    textRes = Res.string.nectarine,
                    imageRes = R.drawable.img_nectarine_half
                )
            )
        ),
        CardPairEntity(
            id = "grapefruit",
            pair = Pair(
                CardEntity.Image(
                    id = "grapefruit_whole",
                    textRes = Res.string.grapefruit,
                    imageRes = R.drawable.img_grapefruit_whole
                ),
                CardEntity.Image(
                    id = "grapefruit_half",
                    textRes = Res.string.grapefruit,
                    imageRes = R.drawable.img_grapefruit_half
                )
            )
        ),
        CardPairEntity(
            id = "blackberry",
            pair = Pair(
                CardEntity.Image(
                    id = "blackberry_whole",
                    textRes = Res.string.blackberry,
                    imageRes = R.drawable.img_blackberry_whole
                ),
                CardEntity.Image(
                    id = "blackberry_half",
                    textRes = Res.string.blackberry,
                    imageRes = R.drawable.img_blackberry_half
                )
            )
        ),
        CardPairEntity(
            id = "fig",
            pair = Pair(
                CardEntity.Image(
                    id = "fig_whole",
                    textRes = Res.string.fig,
                    imageRes = R.drawable.img_fig_whole
                ),
                CardEntity.Image(
                    id = "fig_half",
                    textRes = Res.string.fig,
                    imageRes = R.drawable.img_fig_half
                )
            )
        ),
        CardPairEntity(
            id = "papaya",
            pair = Pair(
                CardEntity.Image(
                    id = "papaya_whole",
                    textRes = Res.string.papaya,
                    imageRes = R.drawable.img_papaya_whole
                ),
                CardEntity.Image(
                    id = "papaya_half",
                    textRes = Res.string.papaya,
                    imageRes = R.drawable.img_papaya_half
                )
            )
        ),
        CardPairEntity(
            id = "cranberry",
            pair = Pair(
                CardEntity.Image(
                    id = "cranberry_whole",
                    textRes = Res.string.cranberry,
                    imageRes = R.drawable.img_cranberry_whole
                ),
                CardEntity.Image(
                    id = "cranberry_half",
                    textRes = Res.string.cranberry,
                    imageRes = R.drawable.img_cranberry_half
                )
            )
        ),
        CardPairEntity(
            id = "guava",
            pair = Pair(
                CardEntity.Image(
                    id = "guava_whole",
                    textRes = Res.string.guava,
                    imageRes = R.drawable.img_guava_whole
                ),
                CardEntity.Image(
                    id = "guava_half",
                    textRes = Res.string.guava,
                    imageRes = R.drawable.img_guava_half
                )
            )
        ),
        CardPairEntity(
            id = "tangerine",
            pair = Pair(
                CardEntity.Image(
                    id = "tangerine_whole",
                    textRes = Res.string.tangerine,
                    imageRes = R.drawable.img_tangerine_whole
                ),
                CardEntity.Image(
                    id = "tangerine_half",
                    textRes = Res.string.tangerine,
                    imageRes = R.drawable.img_tangerine_half
                )
            )
        ),
        CardPairEntity(
            id = "honeydew_melon",
            pair = Pair(
                CardEntity.Image(
                    id = "honeydew_melon_whole",
                    textRes = Res.string.honeydew_melon,
                    imageRes = R.drawable.img_honeydew_melon_whole
                ),
                CardEntity.Image(
                    id = "honeydew_melon_half",
                    textRes = Res.string.honeydew_melon,
                    imageRes = R.drawable.img_honeydew_melon_half
                )
            )
        ),
        CardPairEntity(
            id = "passion_fruit",
            pair = Pair(
                CardEntity.Image(
                    id = "passion_fruit_whole",
                    textRes = Res.string.passion_fruit,
                    imageRes = R.drawable.img_passion_fruit_whole
                ),
                CardEntity.Image(
                    id = "passion_fruit_half",
                    textRes = Res.string.passion_fruit,
                    imageRes = R.drawable.img_passion_fruit_half
                )
            )
        ),
        CardPairEntity(
            id = "lychee",
            pair = Pair(
                CardEntity.Image(
                    id = "lychee_whole",
                    textRes = Res.string.lychee,
                    imageRes = R.drawable.img_lychee_whole
                ),
                CardEntity.Image(
                    id = "lychee_half",
                    textRes = Res.string.lychee,
                    imageRes = R.drawable.img_lychee_half
                )
            )
        ),
        CardPairEntity(
            id = "dragon_fruit",
            pair = Pair(
                CardEntity.Image(
                    id = "dragon_fruit_whole",
                    textRes = Res.string.dragon_fruit,
                    imageRes = R.drawable.img_dragon_fruit_whole
                ),
                CardEntity.Image(
                    id = "dragon_fruit_half",
                    textRes = Res.string.dragon_fruit,
                    imageRes = R.drawable.img_dragon_fruit_half
                )
            )
        ),
        CardPairEntity(
            id = "date",
            pair = Pair(
                CardEntity.Image(
                    id = "date_whole",
                    textRes = Res.string.date,
                    imageRes = R.drawable.img_date_whole
                ),
                CardEntity.Image(
                    id = "date_half",
                    textRes = Res.string.date,
                    imageRes = R.drawable.img_date_half
                )
            )
        ),
        CardPairEntity(
            id = "persimmon",
            pair = Pair(
                CardEntity.Image(
                    id = "persimmon_whole",
                    textRes = Res.string.persimmon,
                    imageRes = R.drawable.img_persimmon_whole
                ),
                CardEntity.Image(
                    id = "persimmon_half",
                    textRes = Res.string.persimmon,
                    imageRes = R.drawable.img_persimmon_half
                )
            )
        ),
        CardPairEntity(
            id = "jackfruit",
            pair = Pair(
                CardEntity.Image(
                    id = "jackfruit_whole",
                    textRes = Res.string.jackfruit,
                    imageRes = R.drawable.img_jackfruit_whole
                ),
                CardEntity.Image(
                    id = "jackfruit_half",
                    textRes = Res.string.jackfruit,
                    imageRes = R.drawable.img_jackfruit_half
                )
            )
        ),
        CardPairEntity(
            id = "star_fruit",
            pair = Pair(
                CardEntity.Image(
                    id = "star_fruit_whole",
                    textRes = Res.string.star_fruit,
                    imageRes = R.drawable.img_star_fruit_whole
                ),
                CardEntity.Image(
                    id = "star_fruit_half",
                    textRes = Res.string.star_fruit,
                    imageRes = R.drawable.img_star_fruit_half
                )
            )
        ),
        CardPairEntity(
            id = "mangosteen",
            pair = Pair(
                CardEntity.Image(
                    id = "mangosteen_whole",
                    textRes = Res.string.mangosteen,
                    imageRes = R.drawable.img_mangosteen_whole
                ),
                CardEntity.Image(
                    id = "mangosteen_half",
                    textRes = Res.string.mangosteen,
                    imageRes = R.drawable.img_mangosteen_half
                )
            )
        ),
        CardPairEntity(
            id = "citron",
            pair = Pair(
                CardEntity.Image(
                    id = "citron_whole",
                    textRes = Res.string.citron,
                    imageRes = R.drawable.img_citron_whole
                ),
                CardEntity.Image(
                    id = "citron_half",
                    textRes = Res.string.citron,
                    imageRes = R.drawable.img_citron_half
                )
            )
        ),
        CardPairEntity(
            id = "durian",
            pair = Pair(
                CardEntity.Image(
                    id = "durian_whole",
                    textRes = Res.string.durian,
                    imageRes = R.drawable.img_durian_whole
                ),
                CardEntity.Image(
                    id = "durian_half",
                    textRes = Res.string.durian,
                    imageRes = R.drawable.img_durian_half
                )
            )
        ),
        CardPairEntity(
            id = "kumquat",
            pair = Pair(
                CardEntity.Image(
                    id = "kumquat_whole",
                    textRes = Res.string.kumquat,
                    imageRes = R.drawable.img_kumquat_whole
                ),
                CardEntity.Image(
                    id = "kumquat_half",
                    textRes = Res.string.kumquat,
                    imageRes = R.drawable.img_kumquat_half
                )
            )
        ),
        CardPairEntity(
            id = "blackcurrant",
            pair = Pair(
                CardEntity.Image(
                    id = "blackcurrant_whole",
                    textRes = Res.string.blackcurrant,
                    imageRes = R.drawable.img_blackcurrant_whole
                ),
                CardEntity.Image(
                    id = "blackcurrant_half",
                    textRes = Res.string.blackcurrant,
                    imageRes = R.drawable.img_blackcurrant_half
                )
            )
        ),
        CardPairEntity(
            id = "gooseberry",
            pair = Pair(
                CardEntity.Image(
                    id = "gooseberry_whole",
                    textRes = Res.string.gooseberry,
                    imageRes = R.drawable.img_gooseberry_whole
                ),
                CardEntity.Image(
                    id = "gooseberry_half",
                    textRes = Res.string.gooseberry,
                    imageRes = R.drawable.img_gooseberry_half
                )
            )
        ),
        CardPairEntity(
            id = "rambutan",
            pair = Pair(
                CardEntity.Image(
                    id = "rambutan_whole",
                    textRes = Res.string.rambutan,
                    imageRes = R.drawable.img_rambutan_whole
                ),
                CardEntity.Image(
                    id = "rambutan_half",
                    textRes = Res.string.rambutan,
                    imageRes = R.drawable.img_rambutan_half
                )
            )
        ),
        CardPairEntity(
            id = "mulberry",
            pair = Pair(
                CardEntity.Image(
                    id = "mulberry_whole",
                    textRes = Res.string.mulberry,
                    imageRes = R.drawable.img_mulberry_whole
                ),
                CardEntity.Image(
                    id = "mulberry_half",
                    textRes = Res.string.mulberry,
                    imageRes = R.drawable.img_mulberry_half
                )
            )
        ),
        CardPairEntity(
            id = "elderberry",
            pair = Pair(
                CardEntity.Image(
                    id = "elderberry_whole",
                    textRes = Res.string.elderberry,
                    imageRes = R.drawable.img_elderberry_whole
                ),
                CardEntity.Image(
                    id = "elderberry_half",
                    textRes = Res.string.elderberry,
                    imageRes = R.drawable.img_elderberry_half
                )
            )
        ),
        CardPairEntity(
            id = "açaí",
            pair = Pair(
                CardEntity.Image(
                    id = "açaí_whole",
                    textRes = Res.string.acai,
                    imageRes = R.drawable.img_acai_whole
                ),
                CardEntity.Image(
                    id = "açaí_half",
                    textRes = Res.string.acai,
                    imageRes = R.drawable.img_acai_half
                )
            )
        ),
        CardPairEntity(
            id = "quince",
            pair = Pair(
                CardEntity.Image(
                    id = "quince_whole",
                    textRes = Res.string.quince,
                    imageRes = R.drawable.img_quince_whole
                ),
                CardEntity.Image(
                    id = "quince_half",
                    textRes = Res.string.quince,
                    imageRes = R.drawable.img_quince_half
                )
            )
        ),
        CardPairEntity(
            id = "redcurrant",
            pair = Pair(
                CardEntity.Image(
                    id = "redcurrant_whole",
                    textRes = Res.string.redcurrant,
                    imageRes = R.drawable.img_redcurrant_whole
                ),
                CardEntity.Image(
                    id = "redcurrant_half",
                    textRes = Res.string.redcurrant,
                    imageRes = R.drawable.img_redcurrant_half
                )
            )
        ),
        CardPairEntity(
            id = "soursop",
            pair = Pair(
                CardEntity.Image(
                    id = "soursop_whole",
                    textRes = Res.string.soursop,
                    imageRes = R.drawable.img_soursop_whole
                ),
                CardEntity.Image(
                    id = "soursop_half",
                    textRes = Res.string.soursop,
                    imageRes = R.drawable.img_soursop_half
                )
            )
        ),
        CardPairEntity(
            id = "kiwano",
            pair = Pair(
                CardEntity.Image(
                    id = "kiwano_whole",
                    textRes = Res.string.kiwano,
                    imageRes = R.drawable.img_kiwano_whole
                ),
                CardEntity.Image(
                    id = "kiwano_half",
                    textRes = Res.string.kiwano,
                    imageRes = R.drawable.img_kiwano_half
                )
            )
        ),
        CardPairEntity(
            id = "longan",
            pair = Pair(
                CardEntity.Image(
                    id = "longan_whole",
                    textRes = Res.string.longan,
                    imageRes = R.drawable.img_longan_whole
                ),
                CardEntity.Image(
                    id = "longan_half",
                    textRes = Res.string.longan,
                    imageRes = R.drawable.img_longan_half
                )
            )
        ),
        CardPairEntity(
            id = "tamarind",
            pair = Pair(
                CardEntity.Image(
                    id = "tamarind_whole",
                    textRes = Res.string.tamarind,
                    imageRes = R.drawable.img_tamarind_whole
                ),
                CardEntity.Image(
                    id = "tamarind_half",
                    textRes = Res.string.tamarind,
                    imageRes = R.drawable.img_tamarind_half
                )
            )
        ),
        CardPairEntity(
            id = "pomelo",
            pair = Pair(
                CardEntity.Image(
                    id = "pomelo_whole",
                    textRes = Res.string.pomelo,
                    imageRes = R.drawable.img_pomelo_whole
                ),
                CardEntity.Image(
                    id = "pomelo_half",
                    textRes = Res.string.pomelo,
                    imageRes = R.drawable.img_pomelo_half
                )
            )
        ),
        CardPairEntity(
            id = "jujube",
            pair = Pair(
                CardEntity.Image(
                    id = "jujube_whole",
                    textRes = Res.string.jujube,
                    imageRes = R.drawable.img_jujube_whole
                ),
                CardEntity.Image(
                    id = "jujube_half",
                    textRes = Res.string.jujube,
                    imageRes = R.drawable.img_jujube_half
                )
            )
        ),
        CardPairEntity(
            id = "cape_gooseberry",
            pair = Pair(
                CardEntity.Image(
                    id = "cape_gooseberry_whole",
                    textRes = Res.string.cape_gooseberry,
                    imageRes = R.drawable.img_cape_gooseberry_whole
                ),
                CardEntity.Image(
                    id = "cape_gooseberry_half",
                    textRes = Res.string.cape_gooseberry,
                    imageRes = R.drawable.img_cape_gooseberry_half
                )
            )
        ),
        CardPairEntity(
            id = "feijoa",
            pair = Pair(
                CardEntity.Image(
                    id = "feijoa_whole",
                    textRes = Res.string.feijoa,
                    imageRes = R.drawable.img_feijoa_whole
                ),
                CardEntity.Image(
                    id = "feijoa_half",
                    textRes = Res.string.feijoa,
                    imageRes = R.drawable.img_feijoa_half
                )
            )
        ),
        CardPairEntity(
            id = "salak",
            pair = Pair(
                CardEntity.Image(
                    id = "salak_whole",
                    textRes = Res.string.salak,
                    imageRes = R.drawable.img_salak_whole
                ),
                CardEntity.Image(
                    id = "salak_half",
                    textRes = Res.string.salak,
                    imageRes = R.drawable.img_salak_half
                )
            )
        ),
        CardPairEntity(
            id = "loquat",
            pair = Pair(
                CardEntity.Image(
                    id = "loquat_whole",
                    textRes = Res.string.loquat,
                    imageRes = R.drawable.img_loquat_whole
                ),
                CardEntity.Image(
                    id = "loquat_half",
                    textRes = Res.string.loquat,
                    imageRes = R.drawable.img_loquat_half
                )
            )
        )
    )
}

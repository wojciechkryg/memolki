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
            id = "potato",
            pair = Pair(
                CardEntity.Image(
                    id = "potato_whole",
                    textRes = Res.string.potato,
                    imageRes = R.drawable.img_potato_whole
                ),
                CardEntity.Image(
                    id = "potato_half",
                    textRes = Res.string.potato,
                    imageRes = R.drawable.img_potato_half
                )
            )
        ),
        CardPairEntity(
            id = "tomato",
            pair = Pair(
                CardEntity.Image(
                    id = "tomato_whole",
                    textRes = Res.string.tomato,
                    imageRes = R.drawable.img_tomato_whole
                ),
                CardEntity.Image(
                    id = "tomato_half",
                    textRes = Res.string.tomato,
                    imageRes = R.drawable.img_tomato_half
                )
            )
        ),
        CardPairEntity(
            id = "onion",
            pair = Pair(
                CardEntity.Image(
                    id = "onion_whole",
                    textRes = Res.string.onion,
                    imageRes = R.drawable.img_onion_whole
                ),
                CardEntity.Image(
                    id = "onion_half",
                    textRes = Res.string.onion,
                    imageRes = R.drawable.img_onion_half
                )
            )
        ),
        CardPairEntity(
            id = "carrot",
            pair = Pair(
                CardEntity.Image(
                    id = "carrot_whole",
                    textRes = Res.string.carrot,
                    imageRes = R.drawable.img_carrot_whole
                ),
                CardEntity.Image(
                    id = "carrot_half",
                    textRes = Res.string.carrot,
                    imageRes = R.drawable.img_carrot_half
                )
            )
        ),
        CardPairEntity(
            id = "lettuce",
            pair = Pair(
                CardEntity.Image(
                    id = "lettuce_whole",
                    textRes = Res.string.lettuce,
                    imageRes = R.drawable.img_lettuce_whole
                ),
                CardEntity.Image(
                    id = "lettuce_half",
                    textRes = Res.string.lettuce,
                    imageRes = R.drawable.img_lettuce_half
                )
            )
        ),
        CardPairEntity(
            id = "bell_pepper",
            pair = Pair(
                CardEntity.Image(
                    id = "bell_pepper_whole",
                    textRes = Res.string.bell_pepper,
                    imageRes = R.drawable.img_bell_pepper_whole
                ),
                CardEntity.Image(
                    id = "bell_pepper_half",
                    textRes = Res.string.bell_pepper,
                    imageRes = R.drawable.img_bell_pepper_half
                )
            )
        ),
        CardPairEntity(
            id = "cucumber",
            pair = Pair(
                CardEntity.Image(
                    id = "cucumber_whole",
                    textRes = Res.string.cucumber,
                    imageRes = R.drawable.img_cucumber_whole
                ),
                CardEntity.Image(
                    id = "cucumber_half",
                    textRes = Res.string.cucumber,
                    imageRes = R.drawable.img_cucumber_half
                )
            )
        ),
        CardPairEntity(
            id = "broccoli",
            pair = Pair(
                CardEntity.Image(
                    id = "broccoli_whole",
                    textRes = Res.string.broccoli,
                    imageRes = R.drawable.img_broccoli_whole
                ),
                CardEntity.Image(
                    id = "broccoli_half",
                    textRes = Res.string.broccoli,
                    imageRes = R.drawable.img_broccoli_half
                )
            )
        ),
        CardPairEntity(
            id = "garlic",
            pair = Pair(
                CardEntity.Image(
                    id = "garlic_whole",
                    textRes = Res.string.garlic,
                    imageRes = R.drawable.img_garlic_whole
                ),
                CardEntity.Image(
                    id = "garlic_half",
                    textRes = Res.string.garlic,
                    imageRes = R.drawable.img_garlic_half
                )
            )
        ),
        CardPairEntity(
            id = "celery",
            pair = Pair(
                CardEntity.Image(
                    id = "celery_whole",
                    textRes = Res.string.celery,
                    imageRes = R.drawable.img_celery_whole
                ),
                CardEntity.Image(
                    id = "celery_half",
                    textRes = Res.string.celery,
                    imageRes = R.drawable.img_celery_half
                )
            )
        ),
        CardPairEntity(
            id = "cabbage",
            pair = Pair(
                CardEntity.Image(
                    id = "cabbage_whole",
                    textRes = Res.string.cabbage,
                    imageRes = R.drawable.img_cabbage_whole
                ),
                CardEntity.Image(
                    id = "cabbage_half",
                    textRes = Res.string.cabbage,
                    imageRes = R.drawable.img_cabbage_half
                )
            )
        ),
        CardPairEntity(
            id = "spinach",
            pair = Pair(
                CardEntity.Image(
                    id = "spinach_whole",
                    textRes = Res.string.spinach,
                    imageRes = R.drawable.img_spinach_whole
                ),
                CardEntity.Image(
                    id = "spinach_half",
                    textRes = Res.string.spinach,
                    imageRes = R.drawable.img_spinach_half
                )
            )
        ),
        CardPairEntity(
            id = "sweetcorn",
            pair = Pair(
                CardEntity.Image(
                    id = "sweetcorn_whole",
                    textRes = Res.string.sweetcorn,
                    imageRes = R.drawable.img_sweetcorn_whole
                ),
                CardEntity.Image(
                    id = "sweetcorn_half",
                    textRes = Res.string.sweetcorn,
                    imageRes = R.drawable.img_sweetcorn_half
                )
            )
        ),
        CardPairEntity(
            id = "green_bean",
            pair = Pair(
                CardEntity.Image(
                    id = "green_bean_whole",
                    textRes = Res.string.green_bean,
                    imageRes = R.drawable.img_green_bean_whole
                ),
                CardEntity.Image(
                    id = "green_bean_half",
                    textRes = Res.string.green_bean,
                    imageRes = R.drawable.img_green_bean_half
                )
            )
        ),
        CardPairEntity(
            id = "cauliflower",
            pair = Pair(
                CardEntity.Image(
                    id = "cauliflower_whole",
                    textRes = Res.string.cauliflower,
                    imageRes = R.drawable.img_cauliflower_whole
                ),
                CardEntity.Image(
                    id = "cauliflower_half",
                    textRes = Res.string.cauliflower,
                    imageRes = R.drawable.img_cauliflower_half
                )
            )
        ),
        CardPairEntity(
            id = "sweet_potato",
            pair = Pair(
                CardEntity.Image(
                    id = "sweet_potato_whole",
                    textRes = Res.string.sweet_potato,
                    imageRes = R.drawable.img_sweet_potato_whole
                ),
                CardEntity.Image(
                    id = "sweet_potato_half",
                    textRes = Res.string.sweet_potato,
                    imageRes = R.drawable.img_sweet_potato_half
                )
            )
        ),
        CardPairEntity(
            id = "pea",
            pair = Pair(
                CardEntity.Image(
                    id = "pea_whole",
                    textRes = Res.string.pea,
                    imageRes = R.drawable.img_pea_whole
                ),
                CardEntity.Image(
                    id = "pea_half",
                    textRes = Res.string.pea,
                    imageRes = R.drawable.img_pea_half
                )
            )
        ),
        CardPairEntity(
            id = "asparagus",
            pair = Pair(
                CardEntity.Image(
                    id = "asparagus_whole",
                    textRes = Res.string.asparagus,
                    imageRes = R.drawable.img_asparagus_whole
                ),
                CardEntity.Image(
                    id = "asparagus_half",
                    textRes = Res.string.asparagus,
                    imageRes = R.drawable.img_asparagus_half
                )
            )
        ),
        CardPairEntity(
            id = "brussels_sprouts",
            pair = Pair(
                CardEntity.Image(
                    id = "brussels_sprouts_whole",
                    textRes = Res.string.brussels_sprout,
                    imageRes = R.drawable.img_brussels_sprout_whole
                ),
                CardEntity.Image(
                    id = "brussels_sprouts_half",
                    textRes = Res.string.brussels_sprout,
                    imageRes = R.drawable.img_brussels_sprout_half
                )
            )
        ),
        CardPairEntity(
            id = "zucchini",
            pair = Pair(
                CardEntity.Image(
                    id = "zucchini_whole",
                    textRes = Res.string.zucchini,
                    imageRes = R.drawable.img_zucchini_whole
                ),
                CardEntity.Image(
                    id = "zucchini_half",
                    textRes = Res.string.zucchini,
                    imageRes = R.drawable.img_zucchini_half
                )
            )
        ),
        CardPairEntity(
            id = "eggplant",
            pair = Pair(
                CardEntity.Image(
                    id = "eggplant_whole",
                    textRes = Res.string.eggplant,
                    imageRes = R.drawable.img_eggplant_whole
                ),
                CardEntity.Image(
                    id = "eggplant_half",
                    textRes = Res.string.eggplant,
                    imageRes = R.drawable.img_eggplant_half
                )
            )
        ),
        CardPairEntity(
            id = "pumpkin",
            pair = Pair(
                CardEntity.Image(
                    id = "pumpkin_whole",
                    textRes = Res.string.pumpkin,
                    imageRes = R.drawable.img_pumpkin_whole
                ),
                CardEntity.Image(
                    id = "pumpkin_half",
                    textRes = Res.string.pumpkin,
                    imageRes = R.drawable.img_pumpkin_half
                )
            )
        ),
        CardPairEntity(
            id = "butternut_squash",
            pair = Pair(
                CardEntity.Image(
                    id = "butternut_squash_whole",
                    textRes = Res.string.butternut_squash,
                    imageRes = R.drawable.img_butternut_squash_whole
                ),
                CardEntity.Image(
                    id = "butternut_squash_half",
                    textRes = Res.string.butternut_squash,
                    imageRes = R.drawable.img_butternut_squash_half
                )
            )
        ),
        CardPairEntity(
            id = "beets",
            pair = Pair(
                CardEntity.Image(
                    id = "beets_whole",
                    textRes = Res.string.beet,
                    imageRes = R.drawable.img_beet_whole
                ),
                CardEntity.Image(
                    id = "beets_half",
                    textRes = Res.string.beet,
                    imageRes = R.drawable.img_beet_half
                )
            )
        ),
        CardPairEntity(
            id = "radish",
            pair = Pair(
                CardEntity.Image(
                    id = "radish_whole",
                    textRes = Res.string.radish,
                    imageRes = R.drawable.img_radish_whole
                ),
                CardEntity.Image(
                    id = "radish_half",
                    textRes = Res.string.radish,
                    imageRes = R.drawable.img_radish_half
                )
            )
        ),
        CardPairEntity(
            id = "kale",
            pair = Pair(
                CardEntity.Image(
                    id = "kale_whole",
                    textRes = Res.string.kale,
                    imageRes = R.drawable.img_kale_whole
                ),
                CardEntity.Image(
                    id = "kale_half",
                    textRes = Res.string.kale,
                    imageRes = R.drawable.img_kale_half
                )
            )
        ),
        CardPairEntity(
            id = "leek",
            pair = Pair(
                CardEntity.Image(
                    id = "leek_whole",
                    textRes = Res.string.leek,
                    imageRes = R.drawable.img_leek_whole
                ),
                CardEntity.Image(
                    id = "leek_half",
                    textRes = Res.string.leek,
                    imageRes = R.drawable.img_leek_half
                )
            )
        ),
        CardPairEntity(
            id = "artichoke",
            pair = Pair(
                CardEntity.Image(
                    id = "artichoke_whole",
                    textRes = Res.string.artichoke,
                    imageRes = R.drawable.img_artichoke_whole
                ),
                CardEntity.Image(
                    id = "artichoke_half",
                    textRes = Res.string.artichoke,
                    imageRes = R.drawable.img_artichoke_half
                )
            )
        ),
        CardPairEntity(
            id = "swiss_chard",
            pair = Pair(
                CardEntity.Image(
                    id = "swiss_chard_whole",
                    textRes = Res.string.swiss_chard,
                    imageRes = R.drawable.img_swiss_chard_whole
                ),
                CardEntity.Image(
                    id = "swiss_chard_half",
                    textRes = Res.string.swiss_chard,
                    imageRes = R.drawable.img_swiss_chard_half
                )
            )
        ),
        CardPairEntity(
            id = "okra",
            pair = Pair(
                CardEntity.Image(
                    id = "okra_whole",
                    textRes = Res.string.okra,
                    imageRes = R.drawable.img_okra_whole
                ),
                CardEntity.Image(
                    id = "okra_half",
                    textRes = Res.string.okra,
                    imageRes = R.drawable.img_okra_half
                )
            )
        ),
        CardPairEntity(
            id = "turnip",
            pair = Pair(
                CardEntity.Image(
                    id = "turnip_whole",
                    textRes = Res.string.turnip,
                    imageRes = R.drawable.img_turnip_whole
                ),
                CardEntity.Image(
                    id = "turnip_half",
                    textRes = Res.string.turnip,
                    imageRes = R.drawable.img_turnip_half
                )
            )
        ),
        CardPairEntity(
            id = "parsnip",
            pair = Pair(
                CardEntity.Image(
                    id = "parsnip_whole",
                    textRes = Res.string.parsnip,
                    imageRes = R.drawable.img_parsnip_whole
                ),
                CardEntity.Image(
                    id = "parsnip_half",
                    textRes = Res.string.parsnip,
                    imageRes = R.drawable.img_parsnip_half
                )
            )
        ),
        CardPairEntity(
            id = "scallion_green_onion",
            pair = Pair(
                CardEntity.Image(
                    id = "scallion_green_onion_whole",
                    textRes = Res.string.scallion,
                    imageRes = R.drawable.img_scallion_whole
                ),
                CardEntity.Image(
                    id = "scallion_green_onion_half",
                    textRes = Res.string.scallion,
                    imageRes = R.drawable.img_scallion_half
                )
            )
        ),
        CardPairEntity(
            id = "arugula",
            pair = Pair(
                CardEntity.Image(
                    id = "arugula_whole",
                    textRes = Res.string.arugula,
                    imageRes = R.drawable.img_arugula_whole
                ),
                CardEntity.Image(
                    id = "arugula_half",
                    textRes = Res.string.arugula,
                    imageRes = R.drawable.img_arugula_half
                )
            )
        ),
        CardPairEntity(
            id = "chili_pepper",
            pair = Pair(
                CardEntity.Image(
                    id = "chili_pepper_whole",
                    textRes = Res.string.chili_pepper,
                    imageRes = R.drawable.img_chili_pepper_whole
                ),
                CardEntity.Image(
                    id = "chili_pepper_half",
                    textRes = Res.string.chili_pepper,
                    imageRes = R.drawable.img_chili_pepper_half
                )
            )
        ),
        CardPairEntity(
            id = "lima_beans",
            pair = Pair(
                CardEntity.Image(
                    id = "lima_beans_whole",
                    textRes = Res.string.lima_bean,
                    imageRes = R.drawable.img_lima_bean_whole
                ),
                CardEntity.Image(
                    id = "lima_beans_half",
                    textRes = Res.string.lima_bean,
                    imageRes = R.drawable.img_lima_bean_half
                )
            )
        ),
        CardPairEntity(
            id = "acorn_squash",
            pair = Pair(
                CardEntity.Image(
                    id = "acorn_squash_whole",
                    textRes = Res.string.acorn_squash,
                    imageRes = R.drawable.img_acorn_squash_whole
                ),
                CardEntity.Image(
                    id = "acorn_squash_half",
                    textRes = Res.string.acorn_squash,
                    imageRes = R.drawable.img_acorn_squash_half
                )
            )
        ),
        CardPairEntity(
            id = "collard_greens",
            pair = Pair(
                CardEntity.Image(
                    id = "collard_greens_whole",
                    textRes = Res.string.collard_greens,
                    imageRes = R.drawable.img_collard_greens_whole
                ),
                CardEntity.Image(
                    id = "collard_greens_half",
                    textRes = Res.string.collard_greens,
                    imageRes = R.drawable.img_collard_greens_half
                )
            )
        ),
        CardPairEntity(
            id = "watercress",
            pair = Pair(
                CardEntity.Image(
                    id = "watercress_whole",
                    textRes = Res.string.watercress,
                    imageRes = R.drawable.img_watercress_whole
                ),
                CardEntity.Image(
                    id = "watercress_half",
                    textRes = Res.string.watercress,
                    imageRes = R.drawable.img_watercress_half
                )
            )
        ),
        CardPairEntity(
            id = "bok_choy",
            pair = Pair(
                CardEntity.Image(
                    id = "bok_choy_whole",
                    textRes = Res.string.bok_choy,
                    imageRes = R.drawable.img_bok_choy_whole
                ),
                CardEntity.Image(
                    id = "bok_choy_half",
                    textRes = Res.string.bok_choy,
                    imageRes = R.drawable.img_bok_choy_half
                )
            )
        ),
        CardPairEntity(
            id = "fennel",
            pair = Pair(
                CardEntity.Image(
                    id = "fennel_whole",
                    textRes = Res.string.fennel,
                    imageRes = R.drawable.img_fennel_whole
                ),
                CardEntity.Image(
                    id = "fennel_half",
                    textRes = Res.string.fennel,
                    imageRes = R.drawable.img_fennel_half
                )
            )
        ),
        CardPairEntity(
            id = "ginger",
            pair = Pair(
                CardEntity.Image(
                    id = "ginger_whole",
                    textRes = Res.string.ginger,
                    imageRes = R.drawable.img_ginger_whole
                ),
                CardEntity.Image(
                    id = "ginger_half",
                    textRes = Res.string.ginger,
                    imageRes = R.drawable.img_ginger_half
                )
            )
        ),
        CardPairEntity(
            id = "radicchio",
            pair = Pair(
                CardEntity.Image(
                    id = "radicchio_whole",
                    textRes = Res.string.radicchio,
                    imageRes = R.drawable.img_radicchio_whole
                ),
                CardEntity.Image(
                    id = "radicchio_half",
                    textRes = Res.string.radicchio,
                    imageRes = R.drawable.img_radicchio_half
                )
            )
        ),
        CardPairEntity(
            id = "endive",
            pair = Pair(
                CardEntity.Image(
                    id = "endive_whole",
                    textRes = Res.string.endive,
                    imageRes = R.drawable.img_endive_whole
                ),
                CardEntity.Image(
                    id = "endive_half",
                    textRes = Res.string.endive,
                    imageRes = R.drawable.img_endive_half
                )
            )
        ),
        CardPairEntity(
            id = "jicama",
            pair = Pair(
                CardEntity.Image(
                    id = "jicama_whole",
                    textRes = Res.string.jicama,
                    imageRes = R.drawable.img_jicama_whole
                ),
                CardEntity.Image(
                    id = "jicama_half",
                    textRes = Res.string.jicama,
                    imageRes = R.drawable.img_jicama_half
                )
            )
        ),
        CardPairEntity(
            id = "kohlrabi",
            pair = Pair(
                CardEntity.Image(
                    id = "kohlrabi_whole",
                    textRes = Res.string.kohlrabi,
                    imageRes = R.drawable.img_kohlrabi_whole
                ),
                CardEntity.Image(
                    id = "kohlrabi_half",
                    textRes = Res.string.kohlrabi,
                    imageRes = R.drawable.img_kohlrabi_half
                )
            )
        ),
        CardPairEntity(
            id = "celeriac",
            pair = Pair(
                CardEntity.Image(
                    id = "celeriac_whole",
                    textRes = Res.string.celeriac,
                    imageRes = R.drawable.img_celeriac_whole
                ),
                CardEntity.Image(
                    id = "celeriac_half",
                    textRes = Res.string.celeriac,
                    imageRes = R.drawable.img_celeriac_half
                )
            )
        ),
        CardPairEntity(
            id = "water_chestnut",
            pair = Pair(
                CardEntity.Image(
                    id = "water_chestnut_whole",
                    textRes = Res.string.water_chestnut,
                    imageRes = R.drawable.img_water_chestnut_whole
                ),
                CardEntity.Image(
                    id = "water_chestnut_half",
                    textRes = Res.string.water_chestnut,
                    imageRes = R.drawable.img_water_chestnut_half
                )
            )
        ),
        CardPairEntity(
            id = "bean_sprouts",
            pair = Pair(
                CardEntity.Image(
                    id = "bean_sprouts_whole",
                    textRes = Res.string.bean_sprouts,
                    imageRes = R.drawable.img_bean_sprouts_whole
                ),
                CardEntity.Image(
                    id = "bean_sprouts_half",
                    textRes = Res.string.bean_sprouts,
                    imageRes = R.drawable.img_bean_sprouts_half
                )
            )
        ),
        CardPairEntity(
            id = "mustard_greens",
            pair = Pair(
                CardEntity.Image(
                    id = "mustard_greens_whole",
                    textRes = Res.string.mustard_greens,
                    imageRes = R.drawable.img_mustard_greens_whole
                ),
                CardEntity.Image(
                    id = "mustard_greens_half",
                    textRes = Res.string.mustard_greens,
                    imageRes = R.drawable.img_mustard_greens_half
                )
            )
        ),
        CardPairEntity(
            id = "fava_bean",
            pair = Pair(
                CardEntity.Image(
                    id = "fava_bean_whole",
                    textRes = Res.string.fava_bean,
                    imageRes = R.drawable.img_fava_bean_whole
                ),
                CardEntity.Image(
                    id = "fava_bean_half",
                    textRes = Res.string.fava_bean,
                    imageRes = R.drawable.img_fava_bean_half
                )
            )
        ),
        CardPairEntity(
            id = "rhubarb",
            pair = Pair(
                CardEntity.Image(
                    id = "rhubarb_whole",
                    textRes = Res.string.rhubarb,
                    imageRes = R.drawable.img_rhubarb_whole
                ),
                CardEntity.Image(
                    id = "rhubarb_half",
                    textRes = Res.string.rhubarb,
                    imageRes = R.drawable.img_rhubarb_half
                )
            )
        ),
        CardPairEntity(
            id = "shallot",
            pair = Pair(
                CardEntity.Image(
                    id = "shallot_whole",
                    textRes = Res.string.shallot,
                    imageRes = R.drawable.img_shallot_whole
                ),
                CardEntity.Image(
                    id = "shallot_half",
                    textRes = Res.string.shallot,
                    imageRes = R.drawable.img_shallot_half
                )
            )
        ),
        CardPairEntity(
            id = "topinambur",
            pair = Pair(
                CardEntity.Image(
                    id = "topinambur_whole",
                    textRes = Res.string.topinambur,
                    imageRes = R.drawable.img_topinambur_whole
                ),
                CardEntity.Image(
                    id = "topinambur_half",
                    textRes = Res.string.topinambur,
                    imageRes = R.drawable.img_topinambur_half
                )
            )
        ),
        CardPairEntity(
            id = "yam",
            pair = Pair(
                CardEntity.Image(
                    id = "yam_whole",
                    textRes = Res.string.yam,
                    imageRes = R.drawable.img_yam_whole
                ),
                CardEntity.Image(
                    id = "yam_half",
                    textRes = Res.string.yam,
                    imageRes = R.drawable.img_yam_half
                )
            )
        ),
        CardPairEntity(
            id = "cassava",
            pair = Pair(
                CardEntity.Image(
                    id = "cassava_whole",
                    textRes = Res.string.cassava,
                    imageRes = R.drawable.img_cassava_whole
                ),
                CardEntity.Image(
                    id = "cassava_half",
                    textRes = Res.string.cassava,
                    imageRes = R.drawable.img_cassava_half
                )
            )
        ),
        CardPairEntity(
            id = "snow_peas",
            pair = Pair(
                CardEntity.Image(
                    id = "snow_peas_whole",
                    textRes = Res.string.snow_peas,
                    imageRes = R.drawable.img_snow_peas_whole
                ),
                CardEntity.Image(
                    id = "snow_peas_half",
                    textRes = Res.string.snow_peas,
                    imageRes = R.drawable.img_snow_peas_half
                )
            )
        ),
        CardPairEntity(
            id = "edamame",
            pair = Pair(
                CardEntity.Image(
                    id = "edamame_whole",
                    textRes = Res.string.edamame,
                    imageRes = R.drawable.img_edamame_whole
                ),
                CardEntity.Image(
                    id = "edamame_half",
                    textRes = Res.string.edamame,
                    imageRes = R.drawable.img_edamame_half
                )
            )
        ),
        CardPairEntity(
            id = "spaghetti_squash",
            pair = Pair(
                CardEntity.Image(
                    id = "spaghetti_squash_whole",
                    textRes = Res.string.spaghetti_squash,
                    imageRes = R.drawable.img_spaghetti_squash_whole
                ),
                CardEntity.Image(
                    id = "spaghetti_squash_half",
                    textRes = Res.string.spaghetti_squash,
                    imageRes = R.drawable.img_spaghetti_squash_half
                )
            )
        ),
        CardPairEntity(
            id = "tomatillo",
            pair = Pair(
                CardEntity.Image(
                    id = "tomatillo_whole",
                    textRes = Res.string.tomatillo,
                    imageRes = R.drawable.img_tomatillo_whole
                ),
                CardEntity.Image(
                    id = "tomatillo_half",
                    textRes = Res.string.tomatillo,
                    imageRes = R.drawable.img_tomatillo_half
                )
            )
        )
    )
}

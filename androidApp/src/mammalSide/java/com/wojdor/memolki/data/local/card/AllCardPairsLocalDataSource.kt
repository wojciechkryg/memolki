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
            id = "domestic_dog",
            pair = Pair(
                CardEntity.Image(
                    id = "domestic_dog_side",
                    textRes = Res.string.domestic_dog,
                    imageRes = R.drawable.img_domestic_dog_side
                ),
                CardEntity.Image(
                    id = "domestic_dog_front",
                    textRes = Res.string.domestic_dog,
                    imageRes = R.drawable.img_domestic_dog_front
                )
            )
        ),
        CardPairEntity(
            id = "domestic_cat",
            pair = Pair(
                CardEntity.Image(
                    id = "domestic_cat_side",
                    textRes = Res.string.domestic_cat,
                    imageRes = R.drawable.img_domestic_cat_side
                ),
                CardEntity.Image(
                    id = "domestic_cat_front",
                    textRes = Res.string.domestic_cat,
                    imageRes = R.drawable.img_domestic_cat_front
                )
            )
        ),
        CardPairEntity(
            id = "african_lion",
            pair = Pair(
                CardEntity.Image(
                    id = "african_lion_side",
                    textRes = Res.string.african_lion,
                    imageRes = R.drawable.img_african_lion_side
                ),
                CardEntity.Image(
                    id = "african_lion_front",
                    textRes = Res.string.african_lion,
                    imageRes = R.drawable.img_african_lion_front
                )
            )
        ),
        CardPairEntity(
            id = "african_bush_elephant",
            pair = Pair(
                CardEntity.Image(
                    id = "african_bush_elephant_side",
                    textRes = Res.string.african_bush_elephant,
                    imageRes = R.drawable.img_african_bush_elephant_side
                ),
                CardEntity.Image(
                    id = "african_bush_elephant_front",
                    textRes = Res.string.african_bush_elephant,
                    imageRes = R.drawable.img_african_bush_elephant_front
                )
            )
        ),
        CardPairEntity(
            id = "siberian_tiger",
            pair = Pair(
                CardEntity.Image(
                    id = "siberian_tiger_side",
                    textRes = Res.string.siberian_tiger,
                    imageRes = R.drawable.img_siberian_tiger_side
                ),
                CardEntity.Image(
                    id = "siberian_tiger_front",
                    textRes = Res.string.siberian_tiger,
                    imageRes = R.drawable.img_siberian_tiger_front
                )
            )
        ),
        CardPairEntity(
            id = "domestic_horse",
            pair = Pair(
                CardEntity.Image(
                    id = "domestic_horse_side",
                    textRes = Res.string.domestic_horse,
                    imageRes = R.drawable.img_domestic_horse_side
                ),
                CardEntity.Image(
                    id = "domestic_horse_front",
                    textRes = Res.string.domestic_horse,
                    imageRes = R.drawable.img_domestic_horse_front
                )
            )
        ),
        CardPairEntity(
            id = "domestic_cow",
            pair = Pair(
                CardEntity.Image(
                    id = "domestic_cow_side",
                    textRes = Res.string.domestic_cow,
                    imageRes = R.drawable.img_domestic_cow_side
                ),
                CardEntity.Image(
                    id = "domestic_cow_front",
                    textRes = Res.string.domestic_cow,
                    imageRes = R.drawable.img_domestic_cow_front
                )
            )
        ),
        CardPairEntity(
            id = "red_kangaroo",
            pair = Pair(
                CardEntity.Image(
                    id = "red_kangaroo_side",
                    textRes = Res.string.red_kangaroo,
                    imageRes = R.drawable.img_red_kangaroo_side
                ),
                CardEntity.Image(
                    id = "red_kangaroo_front",
                    textRes = Res.string.red_kangaroo,
                    imageRes = R.drawable.img_red_kangaroo_front
                )
            )
        ),
        CardPairEntity(
            id = "brown_bear",
            pair = Pair(
                CardEntity.Image(
                    id = "brown_bear_side",
                    textRes = Res.string.brown_bear,
                    imageRes = R.drawable.img_brown_bear_side
                ),
                CardEntity.Image(
                    id = "brown_bear_front",
                    textRes = Res.string.brown_bear,
                    imageRes = R.drawable.img_brown_bear_front
                )
            )
        ),
        CardPairEntity(
            id = "common_bottlenose_dolphin",
            pair = Pair(
                CardEntity.Image(
                    id = "common_bottlenose_dolphin_side",
                    textRes = Res.string.common_bottlenose_dolphin,
                    imageRes = R.drawable.img_common_bottlenose_dolphin_side
                ),
                CardEntity.Image(
                    id = "common_bottlenose_dolphin_front",
                    textRes = Res.string.common_bottlenose_dolphin,
                    imageRes = R.drawable.img_common_bottlenose_dolphin_front
                )
            )
        ),
        CardPairEntity(
            id = "koala",
            pair = Pair(
                CardEntity.Image(
                    id = "koala_side",
                    textRes = Res.string.koala,
                    imageRes = R.drawable.img_koala_side
                ),
                CardEntity.Image(
                    id = "koala_front",
                    textRes = Res.string.koala,
                    imageRes = R.drawable.img_koala_front
                )
            )
        ),
        CardPairEntity(
            id = "giant_panda",
            pair = Pair(
                CardEntity.Image(
                    id = "giant_panda_side",
                    textRes = Res.string.giant_panda,
                    imageRes = R.drawable.img_giant_panda_side
                ),
                CardEntity.Image(
                    id = "giant_panda_front",
                    textRes = Res.string.giant_panda,
                    imageRes = R.drawable.img_giant_panda_front
                )
            )
        ),
        CardPairEntity(
            id = "gray_wolf",
            pair = Pair(
                CardEntity.Image(
                    id = "gray_wolf_side",
                    textRes = Res.string.gray_wolf,
                    imageRes = R.drawable.img_gray_wolf_side
                ),
                CardEntity.Image(
                    id = "gray_wolf_front",
                    textRes = Res.string.gray_wolf,
                    imageRes = R.drawable.img_gray_wolf_front
                )
            )
        ),
        CardPairEntity(
            id = "domestic_goat",
            pair = Pair(
                CardEntity.Image(
                    id = "domestic_goat_side",
                    textRes = Res.string.domestic_goat,
                    imageRes = R.drawable.img_domestic_goat_side
                ),
                CardEntity.Image(
                    id = "domestic_goat_front",
                    textRes = Res.string.domestic_goat,
                    imageRes = R.drawable.img_domestic_goat_front
                )
            )
        ),
        CardPairEntity(
            id = "domestic_sheep",
            pair = Pair(
                CardEntity.Image(
                    id = "domestic_sheep_side",
                    textRes = Res.string.domestic_sheep,
                    imageRes = R.drawable.img_domestic_sheep_side
                ),
                CardEntity.Image(
                    id = "domestic_sheep_front",
                    textRes = Res.string.domestic_sheep,
                    imageRes = R.drawable.img_domestic_sheep_front
                )
            )
        ),
        CardPairEntity(
            id = "giraffe",
            pair = Pair(
                CardEntity.Image(
                    id = "giraffe_side",
                    textRes = Res.string.giraffe,
                    imageRes = R.drawable.img_giraffe_side
                ),
                CardEntity.Image(
                    id = "giraffe_front",
                    textRes = Res.string.giraffe,
                    imageRes = R.drawable.img_giraffe_front
                )
            )
        ),
        CardPairEntity(
            id = "hippopotamus",
            pair = Pair(
                CardEntity.Image(
                    id = "hippopotamus_side",
                    textRes = Res.string.hippopotamus,
                    imageRes = R.drawable.img_hippopotamus_side
                ),
                CardEntity.Image(
                    id = "hippopotamus_front",
                    textRes = Res.string.hippopotamus,
                    imageRes = R.drawable.img_hippopotamus_front
                )
            )
        ),
        CardPairEntity(
            id = "orca",
            pair = Pair(
                CardEntity.Image(
                    id = "orca_side",
                    textRes = Res.string.orca,
                    imageRes = R.drawable.img_orca_side
                ),
                CardEntity.Image(
                    id = "orca_front",
                    textRes = Res.string.orca,
                    imageRes = R.drawable.img_orca_front
                )
            )
        ),
        CardPairEntity(
            id = "common_chimpanzee",
            pair = Pair(
                CardEntity.Image(
                    id = "common_chimpanzee_side",
                    textRes = Res.string.common_chimpanzee,
                    imageRes = R.drawable.img_common_chimpanzee_side
                ),
                CardEntity.Image(
                    id = "common_chimpanzee_front",
                    textRes = Res.string.common_chimpanzee,
                    imageRes = R.drawable.img_common_chimpanzee_front
                )
            )
        ),
        CardPairEntity(
            id = "western_lowland_gorilla",
            pair = Pair(
                CardEntity.Image(
                    id = "western_lowland_gorilla_side",
                    textRes = Res.string.western_lowland_gorilla,
                    imageRes = R.drawable.img_western_lowland_gorilla_side
                ),
                CardEntity.Image(
                    id = "western_lowland_gorilla_front",
                    textRes = Res.string.western_lowland_gorilla,
                    imageRes = R.drawable.img_western_lowland_gorilla_front
                )
            )
        ),
        CardPairEntity(
            id = "bornean_orangutan",
            pair = Pair(
                CardEntity.Image(
                    id = "bornean_orangutan_side",
                    textRes = Res.string.bornean_orangutan,
                    imageRes = R.drawable.img_bornean_orangutan_side
                ),
                CardEntity.Image(
                    id = "bornean_orangutan_front",
                    textRes = Res.string.bornean_orangutan,
                    imageRes = R.drawable.img_bornean_orangutan_front
                )
            )
        ),
        CardPairEntity(
            id = "domestic_pig",
            pair = Pair(
                CardEntity.Image(
                    id = "domestic_pig_side",
                    textRes = Res.string.domestic_pig,
                    imageRes = R.drawable.img_domestic_pig_side
                ),
                CardEntity.Image(
                    id = "domestic_pig_front",
                    textRes = Res.string.domestic_pig,
                    imageRes = R.drawable.img_domestic_pig_front
                )
            )
        ),
        CardPairEntity(
            id = "plains_zebra",
            pair = Pair(
                CardEntity.Image(
                    id = "plains_zebra_side",
                    textRes = Res.string.plains_zebra,
                    imageRes = R.drawable.img_plains_zebra_side
                ),
                CardEntity.Image(
                    id = "plains_zebra_front",
                    textRes = Res.string.plains_zebra,
                    imageRes = R.drawable.img_plains_zebra_front
                )
            )
        ),
        CardPairEntity(
            id = "cheetah",
            pair = Pair(
                CardEntity.Image(
                    id = "cheetah_side",
                    textRes = Res.string.cheetah,
                    imageRes = R.drawable.img_cheetah_side
                ),
                CardEntity.Image(
                    id = "cheetah_front",
                    textRes = Res.string.cheetah,
                    imageRes = R.drawable.img_cheetah_front
                )
            )
        ),
        CardPairEntity(
            id = "white_rhinoceros",
            pair = Pair(
                CardEntity.Image(
                    id = "white_rhinoceros_side",
                    textRes = Res.string.white_rhinoceros,
                    imageRes = R.drawable.img_white_rhinoceros_side
                ),
                CardEntity.Image(
                    id = "white_rhinoceros_front",
                    textRes = Res.string.white_rhinoceros,
                    imageRes = R.drawable.img_white_rhinoceros_front
                )
            )
        ),
        CardPairEntity(
            id = "cougar",
            pair = Pair(
                CardEntity.Image(
                    id = "cougar_side",
                    textRes = Res.string.cougar,
                    imageRes = R.drawable.img_cougar_side
                ),
                CardEntity.Image(
                    id = "cougar_front",
                    textRes = Res.string.cougar,
                    imageRes = R.drawable.img_cougar_front
                )
            )
        ),
        CardPairEntity(
            id = "red_fox",
            pair = Pair(
                CardEntity.Image(
                    id = "red_fox_side",
                    textRes = Res.string.red_fox,
                    imageRes = R.drawable.img_red_fox_side
                ),
                CardEntity.Image(
                    id = "red_fox_front",
                    textRes = Res.string.red_fox,
                    imageRes = R.drawable.img_red_fox_front
                )
            )
        ),
        CardPairEntity(
            id = "polar_bear",
            pair = Pair(
                CardEntity.Image(
                    id = "polar_bear_side",
                    textRes = Res.string.polar_bear,
                    imageRes = R.drawable.img_polar_bear_side
                ),
                CardEntity.Image(
                    id = "polar_bear_front",
                    textRes = Res.string.polar_bear,
                    imageRes = R.drawable.img_polar_bear_front
                )
            )
        ),
        CardPairEntity(
            id = "indian_elephant",
            pair = Pair(
                CardEntity.Image(
                    id = "indian_elephant_side",
                    textRes = Res.string.indian_elephant,
                    imageRes = R.drawable.img_indian_elephant_side
                ),
                CardEntity.Image(
                    id = "indian_elephant_front",
                    textRes = Res.string.indian_elephant,
                    imageRes = R.drawable.img_indian_elephant_front
                )
            )
        ),
        CardPairEntity(
            id = "domestic_rabbit",
            pair = Pair(
                CardEntity.Image(
                    id = "domestic_rabbit_side",
                    textRes = Res.string.domestic_rabbit,
                    imageRes = R.drawable.img_domestic_rabbit_side
                ),
                CardEntity.Image(
                    id = "domestic_rabbit_front",
                    textRes = Res.string.domestic_rabbit,
                    imageRes = R.drawable.img_domestic_rabbit_front
                )
            )
        ),
        CardPairEntity(
            id = "european_hare",
            pair = Pair(
                CardEntity.Image(
                    id = "european_hare_side",
                    textRes = Res.string.european_hare,
                    imageRes = R.drawable.img_european_hare_side
                ),
                CardEntity.Image(
                    id = "european_hare_front",
                    textRes = Res.string.european_hare,
                    imageRes = R.drawable.img_european_hare_front
                )
            )
        ),
        CardPairEntity(
            id = "bactrian_camel",
            pair = Pair(
                CardEntity.Image(
                    id = "bactrian_camel_side",
                    textRes = Res.string.bactrian_camel,
                    imageRes = R.drawable.img_bactrian_camel_side
                ),
                CardEntity.Image(
                    id = "bactrian_camel_front",
                    textRes = Res.string.bactrian_camel,
                    imageRes = R.drawable.img_bactrian_camel_front
                )
            )
        ),
        CardPairEntity(
            id = "american_bison",
            pair = Pair(
                CardEntity.Image(
                    id = "american_bison_side",
                    textRes = Res.string.american_bison,
                    imageRes = R.drawable.img_american_bison_side
                ),
                CardEntity.Image(
                    id = "american_bison_front",
                    textRes = Res.string.american_bison,
                    imageRes = R.drawable.img_american_bison_front
                )
            )
        ),
        CardPairEntity(
            id = "moose",
            pair = Pair(
                CardEntity.Image(
                    id = "moose_side",
                    textRes = Res.string.moose,
                    imageRes = R.drawable.img_moose_side
                ),
                CardEntity.Image(
                    id = "moose_front",
                    textRes = Res.string.moose,
                    imageRes = R.drawable.img_moose_front
                )
            )
        ),
        CardPairEntity(
            id = "wild_boar",
            pair = Pair(
                CardEntity.Image(
                    id = "wild_boar_side",
                    textRes = Res.string.wild_boar,
                    imageRes = R.drawable.img_wild_boar_side
                ),
                CardEntity.Image(
                    id = "wild_boar_front",
                    textRes = Res.string.wild_boar,
                    imageRes = R.drawable.img_wild_boar_front
                )
            )
        ),
        CardPairEntity(
            id = "blue_whale",
            pair = Pair(
                CardEntity.Image(
                    id = "blue_whale_side",
                    textRes = Res.string.blue_whale,
                    imageRes = R.drawable.img_blue_whale_side
                ),
                CardEntity.Image(
                    id = "blue_whale_front",
                    textRes = Res.string.blue_whale,
                    imageRes = R.drawable.img_blue_whale_front
                )
            )
        ),
        CardPairEntity(
            id = "sperm_whale",
            pair = Pair(
                CardEntity.Image(
                    id = "sperm_whale_side",
                    textRes = Res.string.sperm_whale,
                    imageRes = R.drawable.img_sperm_whale_side
                ),
                CardEntity.Image(
                    id = "sperm_whale_front",
                    textRes = Res.string.sperm_whale,
                    imageRes = R.drawable.img_sperm_whale_front
                )
            )
        ),
        CardPairEntity(
            id = "african_buffalo",
            pair = Pair(
                CardEntity.Image(
                    id = "african_buffalo_side",
                    textRes = Res.string.african_buffalo,
                    imageRes = R.drawable.img_african_buffalo_side
                ),
                CardEntity.Image(
                    id = "african_buffalo_front",
                    textRes = Res.string.african_buffalo,
                    imageRes = R.drawable.img_african_buffalo_front
                )
            )
        ),
        CardPairEntity(
            id = "capybara",
            pair = Pair(
                CardEntity.Image(
                    id = "capybara_side",
                    textRes = Res.string.capybara,
                    imageRes = R.drawable.img_capybara_side
                ),
                CardEntity.Image(
                    id = "capybara_front",
                    textRes = Res.string.capybara,
                    imageRes = R.drawable.img_capybara_front
                )
            )
        ),
        CardPairEntity(
            id = "european_beaver",
            pair = Pair(
                CardEntity.Image(
                    id = "european_beaver_side",
                    textRes = Res.string.european_beaver,
                    imageRes = R.drawable.img_european_beaver_side
                ),
                CardEntity.Image(
                    id = "european_beaver_front",
                    textRes = Res.string.european_beaver,
                    imageRes = R.drawable.img_european_beaver_front
                )
            )
        ),
        CardPairEntity(
            id = "eurasian_red_squirrel",
            pair = Pair(
                CardEntity.Image(
                    id = "eurasian_red_squirrel_side",
                    textRes = Res.string.eurasian_red_squirrel,
                    imageRes = R.drawable.img_eurasian_red_squirrel_side
                ),
                CardEntity.Image(
                    id = "eurasian_red_squirrel_front",
                    textRes = Res.string.eurasian_red_squirrel,
                    imageRes = R.drawable.img_eurasian_red_squirrel_front
                )
            )
        ),
        CardPairEntity(
            id = "northern_white_breasted_hedgehog",
            pair = Pair(
                CardEntity.Image(
                    id = "northern_white_breasted_hedgehog_side",
                    textRes = Res.string.northern_white_breasted_hedgehog,
                    imageRes = R.drawable.img_northern_white_breasted_hedgehog_side
                ),
                CardEntity.Image(
                    id = "northern_white_breasted_hedgehog_front",
                    textRes = Res.string.northern_white_breasted_hedgehog,
                    imageRes = R.drawable.img_northern_white_breasted_hedgehog_front
                )
            )
        ),
        CardPairEntity(
            id = "platypus",
            pair = Pair(
                CardEntity.Image(
                    id = "platypus_side",
                    textRes = Res.string.platypus,
                    imageRes = R.drawable.img_platypus_side
                ),
                CardEntity.Image(
                    id = "platypus_front",
                    textRes = Res.string.platypus,
                    imageRes = R.drawable.img_platypus_front
                )
            )
        ),
        CardPairEntity(
            id = "tasmanian_devil",
            pair = Pair(
                CardEntity.Image(
                    id = "tasmanian_devil_side",
                    textRes = Res.string.tasmanian_devil,
                    imageRes = R.drawable.img_tasmanian_devil_side
                ),
                CardEntity.Image(
                    id = "tasmanian_devil_front",
                    textRes = Res.string.tasmanian_devil,
                    imageRes = R.drawable.img_tasmanian_devil_front
                )
            )
        ),
        CardPairEntity(
            id = "meerkat",
            pair = Pair(
                CardEntity.Image(
                    id = "meerkat_side",
                    textRes = Res.string.meerkat,
                    imageRes = R.drawable.img_meerkat_side
                ),
                CardEntity.Image(
                    id = "meerkat_front",
                    textRes = Res.string.meerkat,
                    imageRes = R.drawable.img_meerkat_front
                )
            )
        ),
        CardPairEntity(
            id = "virginia_opossum",
            pair = Pair(
                CardEntity.Image(
                    id = "virginia_opossum_side",
                    textRes = Res.string.virginia_opossum,
                    imageRes = R.drawable.img_virginia_opossum_side
                ),
                CardEntity.Image(
                    id = "virginia_opossum_front",
                    textRes = Res.string.virginia_opossum,
                    imageRes = R.drawable.img_virginia_opossum_front
                )
            )
        ),
        CardPairEntity(
            id = "european_otter",
            pair = Pair(
                CardEntity.Image(
                    id = "european_otter_side",
                    textRes = Res.string.european_otter,
                    imageRes = R.drawable.img_european_otter_side
                ),
                CardEntity.Image(
                    id = "european_otter_front",
                    textRes = Res.string.european_otter,
                    imageRes = R.drawable.img_european_otter_front
                )
            )
        ),
        CardPairEntity(
            id = "red_deer",
            pair = Pair(
                CardEntity.Image(
                    id = "red_deer_side",
                    textRes = Res.string.red_deer,
                    imageRes = R.drawable.img_red_deer_side
                ),
                CardEntity.Image(
                    id = "red_deer_front",
                    textRes = Res.string.red_deer,
                    imageRes = R.drawable.img_red_deer_front
                )
            )
        ),
        CardPairEntity(
            id = "ring_tailed_lemur",
            pair = Pair(
                CardEntity.Image(
                    id = "ring_tailed_lemur_side",
                    textRes = Res.string.ring_tailed_lemur,
                    imageRes = R.drawable.img_ring_tailed_lemur_side
                ),
                CardEntity.Image(
                    id = "ring_tailed_lemur_front",
                    textRes = Res.string.ring_tailed_lemur,
                    imageRes = R.drawable.img_ring_tailed_lemur_front
                )
            )
        ),
        CardPairEntity(
            id = "japanese_macaque",
            pair = Pair(
                CardEntity.Image(
                    id = "japanese_macaque_side",
                    textRes = Res.string.japanese_macaque,
                    imageRes = R.drawable.img_japanese_macaque_side
                ),
                CardEntity.Image(
                    id = "japanese_macaque_front",
                    textRes = Res.string.japanese_macaque,
                    imageRes = R.drawable.img_japanese_macaque_front
                )
            )
        ),
        CardPairEntity(
            id = "common_seal",
            pair = Pair(
                CardEntity.Image(
                    id = "common_seal_side",
                    textRes = Res.string.common_seal,
                    imageRes = R.drawable.img_common_seal_side
                ),
                CardEntity.Image(
                    id = "common_seal_front",
                    textRes = Res.string.common_seal,
                    imageRes = R.drawable.img_common_seal_front
                )
            )
        ),
        CardPairEntity(
            id = "walrus",
            pair = Pair(
                CardEntity.Image(
                    id = "walrus_side",
                    textRes = Res.string.walrus,
                    imageRes = R.drawable.img_walrus_side
                ),
                CardEntity.Image(
                    id = "walrus_front",
                    textRes = Res.string.walrus,
                    imageRes = R.drawable.img_walrus_front
                )
            )
        ),
        CardPairEntity(
            id = "west_indian_manatee",
            pair = Pair(
                CardEntity.Image(
                    id = "west_indian_manatee_side",
                    textRes = Res.string.west_indian_manatee,
                    imageRes = R.drawable.img_west_indian_manatee_side
                ),
                CardEntity.Image(
                    id = "west_indian_manatee_front",
                    textRes = Res.string.west_indian_manatee,
                    imageRes = R.drawable.img_west_indian_manatee_front
                )
            )
        ),
        CardPairEntity(
            id = "dugong",
            pair = Pair(
                CardEntity.Image(
                    id = "dugong_side",
                    textRes = Res.string.dugong,
                    imageRes = R.drawable.img_dugong_side
                ),
                CardEntity.Image(
                    id = "dugong_front",
                    textRes = Res.string.dugong,
                    imageRes = R.drawable.img_dugong_front
                )
            )
        ),
        CardPairEntity(
            id = "giant_anteater",
            pair = Pair(
                CardEntity.Image(
                    id = "giant_anteater_side",
                    textRes = Res.string.giant_anteater,
                    imageRes = R.drawable.img_giant_anteater_side
                ),
                CardEntity.Image(
                    id = "giant_anteater_front",
                    textRes = Res.string.giant_anteater,
                    imageRes = R.drawable.img_giant_anteater_front
                )
            )
        ),
        CardPairEntity(
            id = "brown_throated_sloth",
            pair = Pair(
                CardEntity.Image(
                    id = "brown_throated_sloth_side",
                    textRes = Res.string.brown_throated_sloth,
                    imageRes = R.drawable.img_brown_throated_sloth_side
                ),
                CardEntity.Image(
                    id = "brown_throated_sloth_front",
                    textRes = Res.string.brown_throated_sloth,
                    imageRes = R.drawable.img_brown_throated_sloth_front
                )
            )
        ),
        CardPairEntity(
            id = "narwhal",
            pair = Pair(
                CardEntity.Image(
                    id = "narwhal_side",
                    textRes = Res.string.narwhal,
                    imageRes = R.drawable.img_narwhal_side
                ),
                CardEntity.Image(
                    id = "narwhal_front",
                    textRes = Res.string.narwhal,
                    imageRes = R.drawable.img_narwhal_front
                )
            )
        ),
        CardPairEntity(
            id = "european_badger",
            pair = Pair(
                CardEntity.Image(
                    id = "european_badger_side",
                    textRes = Res.string.european_badger,
                    imageRes = R.drawable.img_european_badger_side
                ),
                CardEntity.Image(
                    id = "european_badger_front",
                    textRes = Res.string.european_badger,
                    imageRes = R.drawable.img_european_badger_front
                )
            )
        ),
        CardPairEntity(
            id = "malayan_tapir",
            pair = Pair(
                CardEntity.Image(
                    id = "malayan_tapir_side",
                    textRes = Res.string.malayan_tapir,
                    imageRes = R.drawable.img_malayan_tapir_side
                ),
                CardEntity.Image(
                    id = "malayan_tapir_front",
                    textRes = Res.string.malayan_tapir,
                    imageRes = R.drawable.img_malayan_tapir_front
                )
            )
        ),
        CardPairEntity(
            id = "white_handed_gibbon",
            pair = Pair(
                CardEntity.Image(
                    id = "white_handed_gibbon_side",
                    textRes = Res.string.white_handed_gibbon,
                    imageRes = R.drawable.img_white_handed_gibbon_side
                ),
                CardEntity.Image(
                    id = "white_handed_gibbon_front",
                    textRes = Res.string.white_handed_gibbon,
                    imageRes = R.drawable.img_white_handed_gibbon_front
                )
            )
        ),
        CardPairEntity(
            id = "alpaca",
            pair = Pair(
                CardEntity.Image(
                    id = "alpaca_side",
                    textRes = Res.string.alpaca,
                    imageRes = R.drawable.img_alpaca_side
                ),
                CardEntity.Image(
                    id = "alpaca_front",
                    textRes = Res.string.alpaca,
                    imageRes = R.drawable.img_alpaca_front
                )
            )
        ),
        CardPairEntity(
            id = "sea_otter",
            pair = Pair(
                CardEntity.Image(
                    id = "sea_otter_side",
                    textRes = Res.string.sea_otter,
                    imageRes = R.drawable.img_sea_otter_side
                ),
                CardEntity.Image(
                    id = "sea_otter_front",
                    textRes = Res.string.sea_otter,
                    imageRes = R.drawable.img_sea_otter_front
                )
            )
        ),
        CardPairEntity(
            id = "common_wombat",
            pair = Pair(
                CardEntity.Image(
                    id = "common_wombat_side",
                    textRes = Res.string.common_wombat,
                    imageRes = R.drawable.img_common_wombat_side
                ),
                CardEntity.Image(
                    id = "common_wombat_front",
                    textRes = Res.string.common_wombat,
                    imageRes = R.drawable.img_common_wombat_front
                )
            )
        ),
        CardPairEntity(
            id = "brown_rat",
            pair = Pair(
                CardEntity.Image(
                    id = "brown_rat_side",
                    textRes = Res.string.brown_rat,
                    imageRes = R.drawable.img_brown_rat_side
                ),
                CardEntity.Image(
                    id = "brown_rat_front",
                    textRes = Res.string.brown_rat,
                    imageRes = R.drawable.img_brown_rat_front
                )
            )
        ),
        CardPairEntity(
            id = "house_mouse",
            pair = Pair(
                CardEntity.Image(
                    id = "house_mouse_side",
                    textRes = Res.string.house_mouse,
                    imageRes = R.drawable.img_house_mouse_side
                ),
                CardEntity.Image(
                    id = "house_mouse_front",
                    textRes = Res.string.house_mouse,
                    imageRes = R.drawable.img_house_mouse_front
                )
            )
        ),
        CardPairEntity(
            id = "european_mole",
            pair = Pair(
                CardEntity.Image(
                    id = "european_mole_side",
                    textRes = Res.string.european_mole,
                    imageRes = R.drawable.img_european_mole_side
                ),
                CardEntity.Image(
                    id = "european_mole_front",
                    textRes = Res.string.european_mole,
                    imageRes = R.drawable.img_european_mole_front
                )
            )
        ),
        CardPairEntity(
            id = "brown_long_eared_bat",
            pair = Pair(
                CardEntity.Image(
                    id = "brown_long_eared_bat_side",
                    textRes = Res.string.brown_long_eared_bat,
                    imageRes = R.drawable.img_brown_long_eared_bat_side
                ),
                CardEntity.Image(
                    id = "brown_long_eared_bat_front",
                    textRes = Res.string.brown_long_eared_bat,
                    imageRes = R.drawable.img_brown_long_eared_bat_front
                )
            )
        ),
        CardPairEntity(
            id = "egyptian_fruit_bat",
            pair = Pair(
                CardEntity.Image(
                    id = "egyptian_fruit_bat_side",
                    textRes = Res.string.egyptian_fruit_bat,
                    imageRes = R.drawable.img_egyptian_fruit_bat_side
                ),
                CardEntity.Image(
                    id = "egyptian_fruit_bat_front",
                    textRes = Res.string.egyptian_fruit_bat,
                    imageRes = R.drawable.img_egyptian_fruit_bat_front
                )
            )
        ),
        CardPairEntity(
            id = "vicuna",
            pair = Pair(
                CardEntity.Image(
                    id = "vicuna_side",
                    textRes = Res.string.vicuna,
                    imageRes = R.drawable.img_vicuna_side
                ),
                CardEntity.Image(
                    id = "vicuna_front",
                    textRes = Res.string.vicuna,
                    imageRes = R.drawable.img_vicuna_front
                )
            )
        ),
        CardPairEntity(
            id = "giant_armadillo",
            pair = Pair(
                CardEntity.Image(
                    id = "giant_armadillo_side",
                    textRes = Res.string.giant_armadillo,
                    imageRes = R.drawable.img_giant_armadillo_side
                ),
                CardEntity.Image(
                    id = "giant_armadillo_front",
                    textRes = Res.string.giant_armadillo,
                    imageRes = R.drawable.img_giant_armadillo_front
                )
            )
        ),
        CardPairEntity(
            id = "arctic_hare",
            pair = Pair(
                CardEntity.Image(
                    id = "arctic_hare_side",
                    textRes = Res.string.arctic_hare,
                    imageRes = R.drawable.img_arctic_hare_side
                ),
                CardEntity.Image(
                    id = "arctic_hare_front",
                    textRes = Res.string.arctic_hare,
                    imageRes = R.drawable.img_arctic_hare_front
                )
            )
        ),
        CardPairEntity(
            id = "fennec_fox",
            pair = Pair(
                CardEntity.Image(
                    id = "fennec_fox_side",
                    textRes = Res.string.fennec_fox,
                    imageRes = R.drawable.img_fennec_fox_side
                ),
                CardEntity.Image(
                    id = "fennec_fox_front",
                    textRes = Res.string.fennec_fox,
                    imageRes = R.drawable.img_fennec_fox_front
                )
            )
        ),
        CardPairEntity(
            id = "cape_hyrax",
            pair = Pair(
                CardEntity.Image(
                    id = "cape_hyrax_side",
                    textRes = Res.string.cape_hyrax,
                    imageRes = R.drawable.img_cape_hyrax_side
                ),
                CardEntity.Image(
                    id = "cape_hyrax_front",
                    textRes = Res.string.cape_hyrax,
                    imageRes = R.drawable.img_cape_hyrax_front
                )
            )
        ),
        CardPairEntity(
            id = "elephant_shrew",
            pair = Pair(
                CardEntity.Image(
                    id = "elephant_shrew_side",
                    textRes = Res.string.elephant_shrew,
                    imageRes = R.drawable.img_elephant_shrew_side
                ),
                CardEntity.Image(
                    id = "elephant_shrew_front",
                    textRes = Res.string.elephant_shrew,
                    imageRes = R.drawable.img_elephant_shrew_front
                )
            )
        ),
        CardPairEntity(
            id = "sunda_colugo",
            pair = Pair(
                CardEntity.Image(
                    id = "sunda_colugo_side",
                    textRes = Res.string.sunda_colugo,
                    imageRes = R.drawable.img_sunda_colugo_side
                ),
                CardEntity.Image(
                    id = "sunda_colugo_front",
                    textRes = Res.string.sunda_colugo,
                    imageRes = R.drawable.img_sunda_colugo_front
                )
            )
        ),
        CardPairEntity(
            id = "chinese_pangolin",
            pair = Pair(
                CardEntity.Image(
                    id = "chinese_pangolin_side",
                    textRes = Res.string.chinese_pangolin,
                    imageRes = R.drawable.img_chinese_pangolin_side
                ),
                CardEntity.Image(
                    id = "chinese_pangolin_front",
                    textRes = Res.string.chinese_pangolin,
                    imageRes = R.drawable.img_chinese_pangolin_front
                )
            )
        ),
        CardPairEntity(
            id = "short_beaked_echidna",
            pair = Pair(
                CardEntity.Image(
                    id = "short_beaked_echidna_side",
                    textRes = Res.string.short_beaked_echidna,
                    imageRes = R.drawable.img_short_beaked_echidna_side
                ),
                CardEntity.Image(
                    id = "short_beaked_echidna_front",
                    textRes = Res.string.short_beaked_echidna,
                    imageRes = R.drawable.img_short_beaked_echidna_front
                )
            )
        ),
        CardPairEntity(
            id = "lesser_chinchilla",
            pair = Pair(
                CardEntity.Image(
                    id = "lesser_chinchilla_side",
                    textRes = Res.string.lesser_chinchilla,
                    imageRes = R.drawable.img_lesser_chinchilla_side
                ),
                CardEntity.Image(
                    id = "lesser_chinchilla_front",
                    textRes = Res.string.lesser_chinchilla,
                    imageRes = R.drawable.img_lesser_chinchilla_front
                )
            )
        ),
        CardPairEntity(
            id = "indian_crested_porcupine",
            pair = Pair(
                CardEntity.Image(
                    id = "indian_crested_porcupine_side",
                    textRes = Res.string.indian_crested_porcupine,
                    imageRes = R.drawable.img_indian_crested_porcupine_side
                ),
                CardEntity.Image(
                    id = "indian_crested_porcupine_front",
                    textRes = Res.string.indian_crested_porcupine,
                    imageRes = R.drawable.img_indian_crested_porcupine_front
                )
            )
        ),
        CardPairEntity(
            id = "golden_mole",
            pair = Pair(
                CardEntity.Image(
                    id = "golden_mole_side",
                    textRes = Res.string.golden_mole,
                    imageRes = R.drawable.img_golden_mole_side
                ),
                CardEntity.Image(
                    id = "golden_mole_front",
                    textRes = Res.string.golden_mole,
                    imageRes = R.drawable.img_golden_mole_front
                )
            )
        )
    )
}

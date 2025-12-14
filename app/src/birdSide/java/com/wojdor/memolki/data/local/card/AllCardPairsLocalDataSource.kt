package com.wojdor.memolki.data.local.card

import com.wojdor.memolki.R
import com.wojdor.memolki.data.entity.CardEntity
import com.wojdor.memolki.data.entity.CardPairEntity
import javax.inject.Inject

class AllCardPairsLocalDataSource @Inject constructor() : AllCardPairsDataSource {

    override fun getAllCardPairs(): List<CardPairEntity> = listOf(
        CardPairEntity(
            id = "golden_eagle",
            pair = Pair(
                CardEntity.Image(
                    id = "golden_eagle_side",
                    textRes = R.string.golden_eagle,
                    imageRes = R.drawable.img_golden_eagle_side
                ),
                CardEntity.Image(
                    id = "golden_eagle_front",
                    textRes = R.string.golden_eagle,
                    imageRes = R.drawable.img_golden_eagle_front
                )
            )
        ),
        CardPairEntity(
            id = "white_stork",
            pair = Pair(
                CardEntity.Image(
                    id = "white_stork_side",
                    textRes = R.string.white_stork,
                    imageRes = R.drawable.img_white_stork_side
                ),
                CardEntity.Image(
                    id = "white_stork_front",
                    textRes = R.string.white_stork,
                    imageRes = R.drawable.img_white_stork_front
                )
            )
        ),
        CardPairEntity(
            id = "house_sparrow",
            pair = Pair(
                CardEntity.Image(
                    id = "house_sparrow_side",
                    textRes = R.string.house_sparrow,
                    imageRes = R.drawable.img_house_sparrow_side
                ),
                CardEntity.Image(
                    id = "house_sparrow_front",
                    textRes = R.string.house_sparrow,
                    imageRes = R.drawable.img_house_sparrow_front
                )
            )
        ),
        CardPairEntity(
            id = "peregrine_falcon",
            pair = Pair(
                CardEntity.Image(
                    id = "peregrine_falcon_side",
                    textRes = R.string.peregrine_falcon,
                    imageRes = R.drawable.img_peregrine_falcon_side
                ),
                CardEntity.Image(
                    id = "peregrine_falcon_front",
                    textRes = R.string.peregrine_falcon,
                    imageRes = R.drawable.img_peregrine_falcon_front
                )
            )
        ),
        CardPairEntity(
            id = "mute_swan",
            pair = Pair(
                CardEntity.Image(
                    id = "mute_swan_side",
                    textRes = R.string.mute_swan,
                    imageRes = R.drawable.img_mute_swan_side
                ),
                CardEntity.Image(
                    id = "mute_swan_front",
                    textRes = R.string.mute_swan,
                    imageRes = R.drawable.img_mute_swan_front
                )
            )
        ),
        CardPairEntity(
            id = "common_raven",
            pair = Pair(
                CardEntity.Image(
                    id = "common_raven_side",
                    textRes = R.string.common_raven,
                    imageRes = R.drawable.img_common_raven_side
                ),
                CardEntity.Image(
                    id = "common_raven_front",
                    textRes = R.string.common_raven,
                    imageRes = R.drawable.img_common_raven_front
                )
            )
        ),
        CardPairEntity(
            id = "long_eared_owl",
            pair = Pair(
                CardEntity.Image(
                    id = "long_eared_owl_side",
                    textRes = R.string.long_eared_owl,
                    imageRes = R.drawable.img_long_eared_owl_side
                ),
                CardEntity.Image(
                    id = "long_eared_owl_front",
                    textRes = R.string.long_eared_owl,
                    imageRes = R.drawable.img_long_eared_owl_front
                )
            )
        ),
        CardPairEntity(
            id = "greater_flamingo",
            pair = Pair(
                CardEntity.Image(
                    id = "greater_flamingo_side",
                    textRes = R.string.greater_flamingo,
                    imageRes = R.drawable.img_greater_flamingo_side
                ),
                CardEntity.Image(
                    id = "greater_flamingo_front",
                    textRes = R.string.greater_flamingo,
                    imageRes = R.drawable.img_greater_flamingo_front
                )
            )
        ),
        CardPairEntity(
            id = "black_chinned_hummingbird",
            pair = Pair(
                CardEntity.Image(
                    id = "black_chinned_hummingbird_side",
                    textRes = R.string.black_chinned_hummingbird,
                    imageRes = R.drawable.img_black_chinned_hummingbird_side
                ),
                CardEntity.Image(
                    id = "black_chinned_hummingbird_front",
                    textRes = R.string.black_chinned_hummingbird,
                    imageRes = R.drawable.img_black_chinned_hummingbird_front
                )
            )
        ),
        CardPairEntity(
            id = "keel_billed_toucan",
            pair = Pair(
                CardEntity.Image(
                    id = "keel_billed_toucan_side",
                    textRes = R.string.keel_billed_toucan,
                    imageRes = R.drawable.img_keel_billed_toucan_side
                ),
                CardEntity.Image(
                    id = "keel_billed_toucan_front",
                    textRes = R.string.keel_billed_toucan,
                    imageRes = R.drawable.img_keel_billed_toucan_front
                )
            )
        ),
        CardPairEntity(
            id = "hyacinth_macaw",
            pair = Pair(
                CardEntity.Image(
                    id = "hyacinth_macaw_side",
                    textRes = R.string.hyacinth_macaw,
                    imageRes = R.drawable.img_hyacinth_macaw_side
                ),
                CardEntity.Image(
                    id = "hyacinth_macaw_front",
                    textRes = R.string.hyacinth_macaw,
                    imageRes = R.drawable.img_hyacinth_macaw_front
                )
            )
        ),
        CardPairEntity(
            id = "common_cuckoo",
            pair = Pair(
                CardEntity.Image(
                    id = "common_cuckoo_side",
                    textRes = R.string.common_cuckoo,
                    imageRes = R.drawable.img_common_cuckoo_side
                ),
                CardEntity.Image(
                    id = "common_cuckoo_front",
                    textRes = R.string.common_cuckoo,
                    imageRes = R.drawable.img_common_cuckoo_front
                )
            )
        ),
        CardPairEntity(
            id = "domestic_goose",
            pair = Pair(
                CardEntity.Image(
                    id = "domestic_goose_side",
                    textRes = R.string.domestic_goose,
                    imageRes = R.drawable.img_domestic_goose_side
                ),
                CardEntity.Image(
                    id = "domestic_goose_front",
                    textRes = R.string.domestic_goose,
                    imageRes = R.drawable.img_domestic_goose_front
                )
            )
        ),
        CardPairEntity(
            id = "common_pheasant",
            pair = Pair(
                CardEntity.Image(
                    id = "common_pheasant_side",
                    textRes = R.string.common_pheasant,
                    imageRes = R.drawable.img_common_pheasant_side
                ),
                CardEntity.Image(
                    id = "common_pheasant_front",
                    textRes = R.string.common_pheasant,
                    imageRes = R.drawable.img_common_pheasant_front
                )
            )
        ),
        CardPairEntity(
            id = "common_ostrich",
            pair = Pair(
                CardEntity.Image(
                    id = "common_ostrich_side",
                    textRes = R.string.common_ostrich,
                    imageRes = R.drawable.img_common_ostrich_side
                ),
                CardEntity.Image(
                    id = "common_ostrich_front",
                    textRes = R.string.common_ostrich,
                    imageRes = R.drawable.img_common_ostrich_front
                )
            )
        ),
        CardPairEntity(
            id = "emperor_penguin",
            pair = Pair(
                CardEntity.Image(
                    id = "emperor_penguin_side",
                    textRes = R.string.emperor_penguin,
                    imageRes = R.drawable.img_emperor_penguin_side
                ),
                CardEntity.Image(
                    id = "emperor_penguin_front",
                    textRes = R.string.emperor_penguin,
                    imageRes = R.drawable.img_emperor_penguin_front
                )
            )
        ),
        CardPairEntity(
            id = "great_white_pelican",
            pair = Pair(
                CardEntity.Image(
                    id = "great_white_pelican_side",
                    textRes = R.string.great_white_pelican,
                    imageRes = R.drawable.img_great_white_pelican_side
                ),
                CardEntity.Image(
                    id = "great_white_pelican_front",
                    textRes = R.string.great_white_pelican,
                    imageRes = R.drawable.img_great_white_pelican_front
                )
            )
        ),
        CardPairEntity(
            id = "wandering_albatross",
            pair = Pair(
                CardEntity.Image(
                    id = "wandering_albatross_side",
                    textRes = R.string.wandering_albatross,
                    imageRes = R.drawable.img_wandering_albatross_side
                ),
                CardEntity.Image(
                    id = "wandering_albatross_front",
                    textRes = R.string.wandering_albatross,
                    imageRes = R.drawable.img_wandering_albatross_front
                )
            )
        ),
        CardPairEntity(
            id = "great_spotted_woodpecker",
            pair = Pair(
                CardEntity.Image(
                    id = "great_spotted_woodpecker_side",
                    textRes = R.string.great_spotted_woodpecker,
                    imageRes = R.drawable.img_great_spotted_woodpecker_side
                ),
                CardEntity.Image(
                    id = "great_spotted_woodpecker_front",
                    textRes = R.string.great_spotted_woodpecker,
                    imageRes = R.drawable.img_great_spotted_woodpecker_front
                )
            )
        ),
        CardPairEntity(
            id = "european_herring_gull",
            pair = Pair(
                CardEntity.Image(
                    id = "european_herring_gull_side",
                    textRes = R.string.european_herring_gull,
                    imageRes = R.drawable.img_european_herring_gull_side
                ),
                CardEntity.Image(
                    id = "european_herring_gull_front",
                    textRes = R.string.european_herring_gull,
                    imageRes = R.drawable.img_european_herring_gull_front
                )
            )
        ),
        CardPairEntity(
            id = "white_tailed_eagle",
            pair = Pair(
                CardEntity.Image(
                    id = "white_tailed_eagle_side",
                    textRes = R.string.white_tailed_eagle,
                    imageRes = R.drawable.img_white_tailed_eagle_side
                ),
                CardEntity.Image(
                    id = "white_tailed_eagle_front",
                    textRes = R.string.white_tailed_eagle,
                    imageRes = R.drawable.img_white_tailed_eagle_front
                )
            )
        ),
        CardPairEntity(
            id = "great_tit",
            pair = Pair(
                CardEntity.Image(
                    id = "great_tit_side",
                    textRes = R.string.great_tit,
                    imageRes = R.drawable.img_great_tit_side
                ),
                CardEntity.Image(
                    id = "great_tit_front",
                    textRes = R.string.great_tit,
                    imageRes = R.drawable.img_great_tit_front
                )
            )
        ),
        CardPairEntity(
            id = "european_robin",
            pair = Pair(
                CardEntity.Image(
                    id = "european_robin_side",
                    textRes = R.string.european_robin,
                    imageRes = R.drawable.img_european_robin_side
                ),
                CardEntity.Image(
                    id = "european_robin_front",
                    textRes = R.string.european_robin,
                    imageRes = R.drawable.img_european_robin_front
                )
            )
        ),
        CardPairEntity(
            id = "northern_lapwing",
            pair = Pair(
                CardEntity.Image(
                    id = "northern_lapwing_side",
                    textRes = R.string.northern_lapwing,
                    imageRes = R.drawable.img_northern_lapwing_side
                ),
                CardEntity.Image(
                    id = "northern_lapwing_front",
                    textRes = R.string.northern_lapwing,
                    imageRes = R.drawable.img_northern_lapwing_front
                )
            )
        ),
        CardPairEntity(
            id = "common_crane",
            pair = Pair(
                CardEntity.Image(
                    id = "common_crane_side",
                    textRes = R.string.common_crane,
                    imageRes = R.drawable.img_common_crane_side
                ),
                CardEntity.Image(
                    id = "common_crane_front",
                    textRes = R.string.common_crane,
                    imageRes = R.drawable.img_common_crane_front
                )
            )
        ),
        CardPairEntity(
            id = "indian_peafowl",
            pair = Pair(
                CardEntity.Image(
                    id = "indian_peafowl_side",
                    textRes = R.string.indian_peafowl,
                    imageRes = R.drawable.img_indian_peafowl_side
                ),
                CardEntity.Image(
                    id = "indian_peafowl_front",
                    textRes = R.string.indian_peafowl,
                    imageRes = R.drawable.img_indian_peafowl_front
                )
            )
        ),
        CardPairEntity(
            id = "domestic_turkey",
            pair = Pair(
                CardEntity.Image(
                    id = "domestic_turkey_side",
                    textRes = R.string.domestic_turkey,
                    imageRes = R.drawable.img_domestic_turkey_side
                ),
                CardEntity.Image(
                    id = "domestic_turkey_front",
                    textRes = R.string.domestic_turkey,
                    imageRes = R.drawable.img_domestic_turkey_front
                )
            )
        ),
        CardPairEntity(
            id = "eurasian_sparrowhawk",
            pair = Pair(
                CardEntity.Image(
                    id = "eurasian_sparrowhawk_side",
                    textRes = R.string.eurasian_sparrowhawk,
                    imageRes = R.drawable.img_eurasian_sparrowhawk_side
                ),
                CardEntity.Image(
                    id = "eurasian_sparrowhawk_front",
                    textRes = R.string.eurasian_sparrowhawk,
                    imageRes = R.drawable.img_eurasian_sparrowhawk_front
                )
            )
        ),
        CardPairEntity(
            id = "eurasian_eagle_owl",
            pair = Pair(
                CardEntity.Image(
                    id = "eurasian_eagle_owl_side",
                    textRes = R.string.eurasian_eagle_owl,
                    imageRes = R.drawable.img_eurasian_eagle_owl_side
                ),
                CardEntity.Image(
                    id = "eurasian_eagle_owl_front",
                    textRes = R.string.eurasian_eagle_owl,
                    imageRes = R.drawable.img_eurasian_eagle_owl_front
                )
            )
        ),
        CardPairEntity(
            id = "andean_condor",
            pair = Pair(
                CardEntity.Image(
                    id = "andean_condor_side",
                    textRes = R.string.andean_condor,
                    imageRes = R.drawable.img_andean_condor_side
                ),
                CardEntity.Image(
                    id = "andean_condor_front",
                    textRes = R.string.andean_condor,
                    imageRes = R.drawable.img_andean_condor_front
                )
            )
        ),
        CardPairEntity(
            id = "marabou_stork",
            pair = Pair(
                CardEntity.Image(
                    id = "marabou_stork_side",
                    textRes = R.string.marabou_stork,
                    imageRes = R.drawable.img_marabou_stork_side
                ),
                CardEntity.Image(
                    id = "marabou_stork_front",
                    textRes = R.string.marabou_stork,
                    imageRes = R.drawable.img_marabou_stork_front
                )
            )
        ),
        CardPairEntity(
            id = "mallard",
            pair = Pair(
                CardEntity.Image(
                    id = "mallard_side",
                    textRes = R.string.mallard,
                    imageRes = R.drawable.img_mallard_side
                ),
                CardEntity.Image(
                    id = "mallard_front",
                    textRes = R.string.mallard,
                    imageRes = R.drawable.img_mallard_front
                )
            )
        ),
        CardPairEntity(
            id = "rock_dove",
            pair = Pair(
                CardEntity.Image(
                    id = "rock_dove_side",
                    textRes = R.string.rock_dove,
                    imageRes = R.drawable.img_rock_dove_side
                ),
                CardEntity.Image(
                    id = "rock_dove_front",
                    textRes = R.string.rock_dove,
                    imageRes = R.drawable.img_rock_dove_front
                )
            )
        ),
        CardPairEntity(
            id = "common_wood_pigeon",
            pair = Pair(
                CardEntity.Image(
                    id = "common_wood_pigeon_side",
                    textRes = R.string.common_wood_pigeon,
                    imageRes = R.drawable.img_common_wood_pigeon_side
                ),
                CardEntity.Image(
                    id = "common_wood_pigeon_front",
                    textRes = R.string.common_wood_pigeon,
                    imageRes = R.drawable.img_common_wood_pigeon_front
                )
            )
        ),
        CardPairEntity(
            id = "canary",
            pair = Pair(
                CardEntity.Image(
                    id = "canary_side",
                    textRes = R.string.canary,
                    imageRes = R.drawable.img_canary_side
                ),
                CardEntity.Image(
                    id = "canary_front",
                    textRes = R.string.canary,
                    imageRes = R.drawable.img_canary_front
                )
            )
        ),
        CardPairEntity(
            id = "budgerigar",
            pair = Pair(
                CardEntity.Image(
                    id = "budgerigar_side",
                    textRes = R.string.budgerigar,
                    imageRes = R.drawable.img_budgerigar_side
                ),
                CardEntity.Image(
                    id = "budgerigar_front",
                    textRes = R.string.budgerigar,
                    imageRes = R.drawable.img_budgerigar_front
                )
            )
        ),
        CardPairEntity(
            id = "sulphur_crested_cockatoo",
            pair = Pair(
                CardEntity.Image(
                    id = "sulphur_crested_cockatoo_side",
                    textRes = R.string.sulphur_crested_cockatoo,
                    imageRes = R.drawable.img_sulphur_crested_cockatoo_side
                ),
                CardEntity.Image(
                    id = "sulphur_crested_cockatoo_front",
                    textRes = R.string.sulphur_crested_cockatoo,
                    imageRes = R.drawable.img_sulphur_crested_cockatoo_front
                )
            )
        ),
        CardPairEntity(
            id = "common_chaffinch",
            pair = Pair(
                CardEntity.Image(
                    id = "common_chaffinch_side",
                    textRes = R.string.common_chaffinch,
                    imageRes = R.drawable.img_common_chaffinch_side
                ),
                CardEntity.Image(
                    id = "common_chaffinch_front",
                    textRes = R.string.common_chaffinch,
                    imageRes = R.drawable.img_common_chaffinch_front
                )
            )
        ),
        CardPairEntity(
            id = "common_blackbird",
            pair = Pair(
                CardEntity.Image(
                    id = "common_blackbird_side",
                    textRes = R.string.common_blackbird,
                    imageRes = R.drawable.img_common_blackbird_side
                ),
                CardEntity.Image(
                    id = "common_blackbird_front",
                    textRes = R.string.common_blackbird,
                    imageRes = R.drawable.img_common_blackbird_front
                )
            )
        ),
        CardPairEntity(
            id = "song_thrush",
            pair = Pair(
                CardEntity.Image(
                    id = "song_thrush_side",
                    textRes = R.string.song_thrush,
                    imageRes = R.drawable.img_song_thrush_side
                ),
                CardEntity.Image(
                    id = "song_thrush_front",
                    textRes = R.string.song_thrush,
                    imageRes = R.drawable.img_song_thrush_front
                )
            )
        ),
        CardPairEntity(
            id = "mandarin_duck",
            pair = Pair(
                CardEntity.Image(
                    id = "mandarin_duck_side",
                    textRes = R.string.mandarin_duck,
                    imageRes = R.drawable.img_mandarin_duck_side
                ),
                CardEntity.Image(
                    id = "mandarin_duck_front",
                    textRes = R.string.mandarin_duck,
                    imageRes = R.drawable.img_mandarin_duck_front
                )
            )
        ),
        CardPairEntity(
            id = "atlantic_puffin",
            pair = Pair(
                CardEntity.Image(
                    id = "atlantic_puffin_side",
                    textRes = R.string.atlantic_puffin,
                    imageRes = R.drawable.img_atlantic_puffin_side
                ),
                CardEntity.Image(
                    id = "atlantic_puffin_front",
                    textRes = R.string.atlantic_puffin,
                    imageRes = R.drawable.img_atlantic_puffin_front
                )
            )
        ),
        CardPairEntity(
            id = "black_stork",
            pair = Pair(
                CardEntity.Image(
                    id = "black_stork_side",
                    textRes = R.string.black_stork,
                    imageRes = R.drawable.img_black_stork_side
                ),
                CardEntity.Image(
                    id = "black_stork_front",
                    textRes = R.string.black_stork,
                    imageRes = R.drawable.img_black_stork_front
                )
            )
        ),
        CardPairEntity(
            id = "great_cormorant",
            pair = Pair(
                CardEntity.Image(
                    id = "great_cormorant_side",
                    textRes = R.string.great_cormorant,
                    imageRes = R.drawable.img_great_cormorant_side
                ),
                CardEntity.Image(
                    id = "great_cormorant_front",
                    textRes = R.string.great_cormorant,
                    imageRes = R.drawable.img_great_cormorant_front
                )
            )
        ),
        CardPairEntity(
            id = "red_kite",
            pair = Pair(
                CardEntity.Image(
                    id = "red_kite_side",
                    textRes = R.string.red_kite,
                    imageRes = R.drawable.img_red_kite_side
                ),
                CardEntity.Image(
                    id = "red_kite_front",
                    textRes = R.string.red_kite,
                    imageRes = R.drawable.img_red_kite_front
                )
            )
        ),
        CardPairEntity(
            id = "tawny_owl",
            pair = Pair(
                CardEntity.Image(
                    id = "tawny_owl_side",
                    textRes = R.string.tawny_owl,
                    imageRes = R.drawable.img_tawny_owl_side
                ),
                CardEntity.Image(
                    id = "tawny_owl_front",
                    textRes = R.string.tawny_owl,
                    imageRes = R.drawable.img_tawny_owl_front
                )
            )
        ),
        CardPairEntity(
            id = "rook",
            pair = Pair(
                CardEntity.Image(
                    id = "rook_side",
                    textRes = R.string.rook,
                    imageRes = R.drawable.img_rook_side
                ),
                CardEntity.Image(
                    id = "rook_front",
                    textRes = R.string.rook,
                    imageRes = R.drawable.img_rook_front
                )
            )
        ),
        CardPairEntity(
            id = "common_starling",
            pair = Pair(
                CardEntity.Image(
                    id = "common_starling_side",
                    textRes = R.string.common_starling,
                    imageRes = R.drawable.img_common_starling_side
                ),
                CardEntity.Image(
                    id = "common_starling_front",
                    textRes = R.string.common_starling,
                    imageRes = R.drawable.img_common_starling_front
                )
            )
        ),
        CardPairEntity(
            id = "eurasian_nuthatch",
            pair = Pair(
                CardEntity.Image(
                    id = "eurasian_nuthatch_side",
                    textRes = R.string.eurasian_nuthatch,
                    imageRes = R.drawable.img_eurasian_nuthatch_side
                ),
                CardEntity.Image(
                    id = "eurasian_nuthatch_front",
                    textRes = R.string.eurasian_nuthatch,
                    imageRes = R.drawable.img_eurasian_nuthatch_front
                )
            )
        ),
        CardPairEntity(
            id = "whitethroat",
            pair = Pair(
                CardEntity.Image(
                    id = "whitethroat_side",
                    textRes = R.string.whitethroat,
                    imageRes = R.drawable.img_whitethroat_side
                ),
                CardEntity.Image(
                    id = "whitethroat_front",
                    textRes = R.string.whitethroat,
                    imageRes = R.drawable.img_whitethroat_front
                )
            )
        ),
        CardPairEntity(
            id = "eurasian_wren",
            pair = Pair(
                CardEntity.Image(
                    id = "eurasian_wren_side",
                    textRes = R.string.eurasian_wren,
                    imageRes = R.drawable.img_eurasian_wren_side
                ),
                CardEntity.Image(
                    id = "eurasian_wren_front",
                    textRes = R.string.eurasian_wren,
                    imageRes = R.drawable.img_eurasian_wren_front
                )
            )
        ),
        CardPairEntity(
            id = "northern_wheatear",
            pair = Pair(
                CardEntity.Image(
                    id = "northern_wheatear_side",
                    textRes = R.string.northern_wheatear,
                    imageRes = R.drawable.img_northern_wheatear_side
                ),
                CardEntity.Image(
                    id = "northern_wheatear_front",
                    textRes = R.string.northern_wheatear,
                    imageRes = R.drawable.img_northern_wheatear_front
                )
            )
        ),
        CardPairEntity(
            id = "white_wagtail",
            pair = Pair(
                CardEntity.Image(
                    id = "white_wagtail_side",
                    textRes = R.string.white_wagtail,
                    imageRes = R.drawable.img_white_wagtail_side
                ),
                CardEntity.Image(
                    id = "white_wagtail_front",
                    textRes = R.string.white_wagtail,
                    imageRes = R.drawable.img_white_wagtail_front
                )
            )
        ),
        CardPairEntity(
            id = "common_nightingale",
            pair = Pair(
                CardEntity.Image(
                    id = "common_nightingale_side",
                    textRes = R.string.common_nightingale,
                    imageRes = R.drawable.img_common_nightingale_side
                ),
                CardEntity.Image(
                    id = "common_nightingale_front",
                    textRes = R.string.common_nightingale,
                    imageRes = R.drawable.img_common_nightingale_front
                )
            )
        ),
        CardPairEntity(
            id = "eurasian_coot",
            pair = Pair(
                CardEntity.Image(
                    id = "eurasian_coot_side",
                    textRes = R.string.eurasian_coot,
                    imageRes = R.drawable.img_eurasian_coot_side
                ),
                CardEntity.Image(
                    id = "eurasian_coot_front",
                    textRes = R.string.eurasian_coot,
                    imageRes = R.drawable.img_eurasian_coot_front
                )
            )
        ),
        CardPairEntity(
            id = "great_crested_grebe",
            pair = Pair(
                CardEntity.Image(
                    id = "great_crested_grebe_side",
                    textRes = R.string.great_crested_grebe,
                    imageRes = R.drawable.img_great_crested_grebe_side
                ),
                CardEntity.Image(
                    id = "great_crested_grebe_front",
                    textRes = R.string.great_crested_grebe,
                    imageRes = R.drawable.img_great_crested_grebe_front
                )
            )
        ),
        CardPairEntity(
            id = "grey_heron",
            pair = Pair(
                CardEntity.Image(
                    id = "grey_heron_side",
                    textRes = R.string.grey_heron,
                    imageRes = R.drawable.img_grey_heron_side
                ),
                CardEntity.Image(
                    id = "grey_heron_front",
                    textRes = R.string.grey_heron,
                    imageRes = R.drawable.img_grey_heron_front
                )
            )
        ),
        CardPairEntity(
            id = "eurasian_bittern",
            pair = Pair(
                CardEntity.Image(
                    id = "eurasian_bittern_side",
                    textRes = R.string.eurasian_bittern,
                    imageRes = R.drawable.img_eurasian_bittern_side
                ),
                CardEntity.Image(
                    id = "eurasian_bittern_front",
                    textRes = R.string.eurasian_bittern,
                    imageRes = R.drawable.img_eurasian_bittern_front
                )
            )
        ),
        CardPairEntity(
            id = "common_tern",
            pair = Pair(
                CardEntity.Image(
                    id = "common_tern_side",
                    textRes = R.string.common_tern,
                    imageRes = R.drawable.img_common_tern_side
                ),
                CardEntity.Image(
                    id = "common_tern_front",
                    textRes = R.string.common_tern,
                    imageRes = R.drawable.img_common_tern_front
                )
            )
        ),
        CardPairEntity(
            id = "northern_gannet",
            pair = Pair(
                CardEntity.Image(
                    id = "northern_gannet_side",
                    textRes = R.string.northern_gannet,
                    imageRes = R.drawable.img_northern_gannet_side
                ),
                CardEntity.Image(
                    id = "northern_gannet_front",
                    textRes = R.string.northern_gannet,
                    imageRes = R.drawable.img_northern_gannet_front
                )
            )
        ),
        CardPairEntity(
            id = "european_golden_plover",
            pair = Pair(
                CardEntity.Image(
                    id = "european_golden_plover_side",
                    textRes = R.string.european_golden_plover,
                    imageRes = R.drawable.img_european_golden_plover_side
                ),
                CardEntity.Image(
                    id = "european_golden_plover_front",
                    textRes = R.string.european_golden_plover,
                    imageRes = R.drawable.img_european_golden_plover_front
                )
            )
        ),
        CardPairEntity(
            id = "european_bee_eater",
            pair = Pair(
                CardEntity.Image(
                    id = "european_bee_eater_side",
                    textRes = R.string.european_bee_eater,
                    imageRes = R.drawable.img_european_bee_eater_side
                ),
                CardEntity.Image(
                    id = "european_bee_eater_front",
                    textRes = R.string.european_bee_eater,
                    imageRes = R.drawable.img_european_bee_eater_front
                )
            )
        ),
        CardPairEntity(
            id = "european_roller",
            pair = Pair(
                CardEntity.Image(
                    id = "european_roller_side",
                    textRes = R.string.european_roller,
                    imageRes = R.drawable.img_european_roller_side
                ),
                CardEntity.Image(
                    id = "european_roller_front",
                    textRes = R.string.european_roller,
                    imageRes = R.drawable.img_european_roller_front
                )
            )
        ),
        CardPairEntity(
            id = "common_kingfisher",
            pair = Pair(
                CardEntity.Image(
                    id = "common_kingfisher_side",
                    textRes = R.string.common_kingfisher,
                    imageRes = R.drawable.img_common_kingfisher_side
                ),
                CardEntity.Image(
                    id = "common_kingfisher_front",
                    textRes = R.string.common_kingfisher,
                    imageRes = R.drawable.img_common_kingfisher_front
                )
            )
        ),
        CardPairEntity(
            id = "eurasian_hoopoe",
            pair = Pair(
                CardEntity.Image(
                    id = "eurasian_hoopoe_side",
                    textRes = R.string.eurasian_hoopoe,
                    imageRes = R.drawable.img_eurasian_hoopoe_side
                ),
                CardEntity.Image(
                    id = "eurasian_hoopoe_front",
                    textRes = R.string.eurasian_hoopoe,
                    imageRes = R.drawable.img_eurasian_hoopoe_front
                )
            )
        ),
        CardPairEntity(
            id = "black_cuckoo",
            pair = Pair(
                CardEntity.Image(
                    id = "black_cuckoo_side",
                    textRes = R.string.black_cuckoo,
                    imageRes = R.drawable.img_black_cuckoo_side
                ),
                CardEntity.Image(
                    id = "black_cuckoo_front",
                    textRes = R.string.black_cuckoo,
                    imageRes = R.drawable.img_black_cuckoo_front
                )
            )
        ),
        CardPairEntity(
            id = "european_turtle_dove",
            pair = Pair(
                CardEntity.Image(
                    id = "european_turtle_dove_side",
                    textRes = R.string.european_turtle_dove,
                    imageRes = R.drawable.img_european_turtle_dove_side
                ),
                CardEntity.Image(
                    id = "european_turtle_dove_front",
                    textRes = R.string.european_turtle_dove,
                    imageRes = R.drawable.img_european_turtle_dove_front
                )
            )
        ),
        CardPairEntity(
            id = "common_swift",
            pair = Pair(
                CardEntity.Image(
                    id = "common_swift_side",
                    textRes = R.string.common_swift,
                    imageRes = R.drawable.img_common_swift_side
                ),
                CardEntity.Image(
                    id = "common_swift_front",
                    textRes = R.string.common_swift,
                    imageRes = R.drawable.img_common_swift_front
                )
            )
        ),
        CardPairEntity(
            id = "emu",
            pair = Pair(
                CardEntity.Image(
                    id = "emu_side",
                    textRes = R.string.emu,
                    imageRes = R.drawable.img_emu_side
                ),
                CardEntity.Image(
                    id = "emu_front",
                    textRes = R.string.emu,
                    imageRes = R.drawable.img_emu_front
                )
            )
        ),
        CardPairEntity(
            id = "southern_cassowary",
            pair = Pair(
                CardEntity.Image(
                    id = "southern_cassowary_side",
                    textRes = R.string.southern_cassowary,
                    imageRes = R.drawable.img_southern_cassowary_side
                ),
                CardEntity.Image(
                    id = "southern_cassowary_front",
                    textRes = R.string.southern_cassowary,
                    imageRes = R.drawable.img_southern_cassowary_front
                )
            )
        ),
        CardPairEntity(
            id = "brown_kiwi",
            pair = Pair(
                CardEntity.Image(
                    id = "brown_kiwi_side",
                    textRes = R.string.brown_kiwi,
                    imageRes = R.drawable.img_brown_kiwi_side
                ),
                CardEntity.Image(
                    id = "brown_kiwi_front",
                    textRes = R.string.brown_kiwi,
                    imageRes = R.drawable.img_brown_kiwi_front
                )
            )
        ),
        CardPairEntity(
            id = "chicken",
            pair = Pair(
                CardEntity.Image(
                    id = "chicken_side",
                    textRes = R.string.chicken,
                    imageRes = R.drawable.img_chicken_side
                ),
                CardEntity.Image(
                    id = "chicken_front",
                    textRes = R.string.chicken,
                    imageRes = R.drawable.img_chicken_front
                )
            )
        ),
        CardPairEntity(
            id = "harpy_eagle",
            pair = Pair(
                CardEntity.Image(
                    id = "harpy_eagle_side",
                    textRes = R.string.harpy_eagle,
                    imageRes = R.drawable.img_harpy_eagle_side
                ),
                CardEntity.Image(
                    id = "harpy_eagle_front",
                    textRes = R.string.harpy_eagle,
                    imageRes = R.drawable.img_harpy_eagle_front
                )
            )
        ),
        CardPairEntity(
            id = "hooded_crow",
            pair = Pair(
                CardEntity.Image(
                    id = "hooded_crow_side",
                    textRes = R.string.hooded_crow,
                    imageRes = R.drawable.img_hooded_crow_side
                ),
                CardEntity.Image(
                    id = "hooded_crow_front",
                    textRes = R.string.hooded_crow,
                    imageRes = R.drawable.img_hooded_crow_front
                )
            )
        ),
        CardPairEntity(
            id = "eurasian_bullfinch",
            pair = Pair(
                CardEntity.Image(
                    id = "eurasian_bullfinch_side",
                    textRes = R.string.eurasian_bullfinch,
                    imageRes = R.drawable.img_eurasian_bullfinch_side
                ),
                CardEntity.Image(
                    id = "eurasian_bullfinch_front",
                    textRes = R.string.eurasian_bullfinch,
                    imageRes = R.drawable.img_eurasian_bullfinch_front
                )
            )
        ),
        CardPairEntity(
            id = "european_goldfinch",
            pair = Pair(
                CardEntity.Image(
                    id = "european_goldfinch_side",
                    textRes = R.string.european_goldfinch,
                    imageRes = R.drawable.img_european_goldfinch_side
                ),
                CardEntity.Image(
                    id = "european_goldfinch_front",
                    textRes = R.string.european_goldfinch,
                    imageRes = R.drawable.img_european_goldfinch_front
                )
            )
        ),
        CardPairEntity(
            id = "eurasian_skylark",
            pair = Pair(
                CardEntity.Image(
                    id = "eurasian_skylark_side",
                    textRes = R.string.eurasian_skylark,
                    imageRes = R.drawable.img_eurasian_skylark_side
                ),
                CardEntity.Image(
                    id = "eurasian_skylark_front",
                    textRes = R.string.eurasian_skylark,
                    imageRes = R.drawable.img_eurasian_skylark_front
                )
            )
        ),
        CardPairEntity(
            id = "barn_owl",
            pair = Pair(
                CardEntity.Image(
                    id = "barn_owl_side",
                    textRes = R.string.barn_owl,
                    imageRes = R.drawable.img_barn_owl_side
                ),
                CardEntity.Image(
                    id = "barn_owl_front",
                    textRes = R.string.barn_owl,
                    imageRes = R.drawable.img_barn_owl_front
                )
            )
        ),
        CardPairEntity(
            id = "eurasian_golden_oriole",
            pair = Pair(
                CardEntity.Image(
                    id = "eurasian_golden_oriole_side",
                    textRes = R.string.eurasian_golden_oriole,
                    imageRes = R.drawable.img_eurasian_golden_oriole_side
                ),
                CardEntity.Image(
                    id = "eurasian_golden_oriole_front",
                    textRes = R.string.eurasian_golden_oriole,
                    imageRes = R.drawable.img_eurasian_golden_oriole_front
                )
            )
        ),
        CardPairEntity(
            id = "secretary_bird",
            pair = Pair(
                CardEntity.Image(
                    id = "secretary_bird_side",
                    textRes = R.string.secretary_bird,
                    imageRes = R.drawable.img_secretary_bird_side
                ),
                CardEntity.Image(
                    id = "secretary_bird_front",
                    textRes = R.string.secretary_bird,
                    imageRes = R.drawable.img_secretary_bird_front
                )
            )
        )
    )
}

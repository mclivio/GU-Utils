package com.donc.gu_utils.data.models

import com.donc.gu_utils.util.DeckBuilder

class Deck(val god: String){
    var libraryIds = mutableListOf<String>()
    val formatCode = 1
    var domain: Int = 0

    init {
        domain = DeckBuilder.encodeGod(god)
    }

    fun addCard(libId: String, rarity: String, cardGod: String) : Int{
        return if (cardGod == god){
            if (libraryIds.count{it == libId} < 2 && libraryIds.size < 30 && rarity != "legendary") {
                libraryIds.add(libId)
                1
            } else if (libraryIds.count{it == libId} == 0 && libraryIds.size < 30 && rarity == "legendary") {
                libraryIds.add(libId)
                1
            } else 0
        }
        //Decks in Gods Unchained consist of 30 cards. If a card rarity is below legendary, you can
        //have up to 2 instances of it in your deck, but if the rarity is legandary you can only
        //have one of it in it.
        else 0
    }

    fun removeCard(libId: String) : Int{
        if (libraryIds.contains(libId)){
            libraryIds.remove(libId)
            return 1
        } else return 0
    }
}
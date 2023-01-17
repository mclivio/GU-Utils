package com.donc.gu_utils.data.models

import com.donc.gu_utils.util.DeckBuilder

class Deck(val god: String){
    var libraryIds = mutableListOf<String>()
    var cardList = mutableListOf<Record>()
    val formatCode = 1
    var domain: Int = 0

    init {
        domain = DeckBuilder.encodeGod(god)
    }

    fun addCard(entry:Record) : Int{
        return if (entry.god == god || entry.god == "neutral"){
            if (libraryIds.count{it == entry.lib_id} < 2 && libraryIds.size < 30 && entry.rarity != "legendary") {
                cardList.add(entry)
                libraryIds.add(entry.lib_id)
                DeckBuilder.saveDeck(this)
                1
            } else if (libraryIds.count{it == entry.lib_id} == 0 && libraryIds.size < 30 && entry.rarity == "legendary") {
                cardList.add(entry)
                libraryIds.add(entry.lib_id)
                DeckBuilder.saveDeck(this)
                1
            } else 0
        }
        //Decks in Gods Unchained consist of 30 cards. If a card rarity is below legendary, you can
        //have up to 2 instances of it in your deck, but if the rarity is legandary you can only
        //have one of it in it.
        else 0
    }

    fun removeCard(entry: Record) : Int{
        return if (libraryIds.contains(entry.lib_id)){
            cardList.remove(entry)
            libraryIds.remove(entry.lib_id)
            DeckBuilder.saveDeck(this)
            1
        } else 0
    }
}
package com.donc.gu_utils.util

import com.donc.gu_utils.data.models.Deck

object DeckBuilder {

    fun encodeGod(god: String): Int {
        var domain = 0
        when (god) {
            "death" -> domain = 1
            "deception" -> domain = 2
            "light" -> domain = 3
            "magic" -> domain = 4
            "nature" -> domain = 5
            "war" -> domain = 6
        }
        return domain
    }
    fun decodeGod(domain: String): String {
        var god = ""
        when (domain) {
            "1" -> god = "death"
            "2" -> god = "deception"
            "3" -> god = "light"
            "4" -> god = "magic"
            "5" -> god = "nature"
            "6" -> god = "war"
        }
        return god
    }

    /* A library ID has the following format: LY-XXX
    L -> Prefix meaning Library.
    Y -> Set ID. "core" = 1, "genesis" = 2 .. "order" 8
    XXX -> Card ID in the specific set.
    Example: Finnian The Fruitbearer has the LibId: L8-018
    This means it's the 18th card of the "order" set.
    */
    fun getSetId(libraryId: String): String {
        return libraryId[1].toString()
    }

    fun getCardId(libraryId: String): String {
        return libraryId.split("-")[1]
        //This would split L8-018 in a listOf("L8", "018") thus getting the element at index 1
        //would return the "018"
    }

    fun encodeLibraryId(libId: String): String {
        /* This one took me a while to figure out as there was no documentation anywhere on how
        the card's library_id gets coded.
        A library_id gets coded into 3 letters:
        LY-XXX -> A B C
        A = Y
        B = XXX / 52
        C = XXX % 52
        Since 26 is the length of the American alphabet, this 52 is the length if we take both uppercase and lowercase
        letters into consideration. The result of each operation is the index of the character in a 52 letters alphabet.
        This was deduced by trying on paper to crack the following ids: L8-018(IAS), L8-165(IDJ), L2-306(CFu)
        L8-018 -> ABC. [A = 8], [B = 018 / 52 = 0], [C = 18 % 52 = 18] -> 8 0 18 -> I A S
        L8-165 -> IDJ. [A = 8], [B = 165 / 52 = 3], [C = 165 % 52 = 9] -> 8 3 9 -> I D J
        L2-306 -> CFu. [A = 2], [B = 306 / 52 = 5], [C = 306 % 52 = 46] -> 2 5 46 -> C F u
         */
        val alphabet = Alphabet.BASE52.characters
        var codedId : String = ""
        val a : Char = alphabet[getSetId(libId).toInt()]
        val b : Char = alphabet[getCardId(libId).toInt() / 52]
        val c : Char = alphabet[getCardId(libId).toInt() % 52]
        codedId = "$a$b$c"
        return codedId
    }

    fun decodeLibraryId(codedId: String): String {
        val alphabet = Alphabet.BASE52.characters
        var libraryId : String = "L"
        val y : String = alphabet.indexOf(codedId[0]).toString()
        val xxx: String = (alphabet.indexOf(codedId[1]) * 52).toString()
        //Since the second letter of a coded Id is equal to XXX / 52 we can just do the inverse
        //operation to figure out the value of xxx.
        // B = XXX / 52 -> XXX = B * 52
        libraryId = "$libraryId$y-$xxx"
        return libraryId
    }

    fun encodeDeck(deck: Deck): String{
        //A Deck code has the following format:
        //Header_Version_God_LibraryIds
        //Header = GU
        //Version = 1
        //God = 1/2/3/4/5/6
        //LibraryIds = {"AAA""BBB""CCC"}
        //Example: GU_1_5_CATCATCAsCAuCAuCAvCAxCBxCBxCByCByCCOCCOCEyCEyIARIARIASIATIATICHICHIDVKBRKBRKBTKBWKDlHAEHAE
        var codedDeck : String = "GU_"
        codedDeck += "${deck.formatCode}_"
        codedDeck += "${deck.domain}_"
        deck.libraryIds.forEach {
            codedDeck += encodeLibraryId(it)
        }
        return codedDeck
    }
/*
    fun decodeDeck(codedDeck: String): Deck{
        var god : String = decodeGod(codedDeck[5].toString())
        var deck : Deck = Deck(god)
        val chunkedIds = codedDeck.substring(7).chunked(3)
        chunkedIds.forEach {
            deck.addCard(it)
            //A Deck needs a LibraryID and Rarity to add it to the list. However you cannot get
            //Rarity just from a LibraryID, you need to get a Proto from the API where lib_id=LibraryID
            //and get the Rarity from that Proto. However, the API doesn't let you get it / doesn't work.
            //If you try the endpoint https://api.godsunchained.com/v0/proto?lib_id=l8-018
            //It shows 1542 result rather than just Finnian. Therefore it's same to assume that the
            //endpoint doesn't work or hasn't been implemented yet. Until then, this function cannot be used.
        }
        return deck
    }
 */
}

enum class Alphabet (val characters: String){
    //BASE10("0123456789"),
    BASE52("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz")
}
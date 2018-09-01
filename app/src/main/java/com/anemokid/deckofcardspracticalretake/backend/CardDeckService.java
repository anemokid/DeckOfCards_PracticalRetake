package com.anemokid.deckofcardspracticalretake.backend;

import com.anemokid.deckofcardspracticalretake.model.SelectedCardsJsonResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CardDeckService
{

    // private members:
    static final String deck_id = "";

    @GET("/deck/{deck_id}/draw/")
        Call<SelectedCardsJsonResponse> getSelectedCards(
                @Path("deck_id") String deck_id,
                @Query("count") int numOfCards);
}

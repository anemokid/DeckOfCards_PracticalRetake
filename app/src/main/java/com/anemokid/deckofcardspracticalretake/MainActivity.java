package com.anemokid.deckofcardspracticalretake;

import android.content.Context;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.anemokid.deckofcardspracticalretake.backend.CardDeckAdapter;
import com.anemokid.deckofcardspracticalretake.backend.CardDeckService;
import com.anemokid.deckofcardspracticalretake.model.Card;
import com.anemokid.deckofcardspracticalretake.model.SelectedCardsJsonResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    // private members:
    private final static String LOG_TAG = "LOG_TAG: Main Activity";
    private TextView m_tv_NumberOfCardsRemaining;
    private Button m_btn_ShuffleNewDeck;
    private EditText m_et_SpecifiedCards;
    private Button m_btn_DrawCards;
    private int fullDeck = 52;

    // card deck values:
    private List<Card> cardDeck;     // LIST OF CARD DECK
    private static String deck_id; // DECK ID
    private int currentCount = 52; // current count OF CARDS:
    private int numOfUsersValue; // USER SELECTED VALUE

    // cards remaining formatting:
    private String displayCount = String.format("Cards Remaining: %s", currentCount); // String placeholder that updates the Cards Remaining text
    private String displayUserValueValidationError = String.format("There are only %s, cards remaining", currentCount); // String placeholder that updates the Cards Remaining text
    private String displaySelectedCard;

    // recycler view imports:
    CardDeckAdapter cardDeckAdapter;

    // card json response models:
    SelectedCardsJsonResponse shuffledDeck;

    // retrofit imports:
    private static Retrofit retrofit = null;
    private RecyclerView m_RV_CardDeck = null;

    // networking imports:
    public static final String SHUFFLE_CARD_DECK_BASE_URL = "https://deckofcardsapi.com/api/deck/new/shuffle/";
    public final String deckOfCards_BASE_URL = "https://deckofcardsapi.com/api/";
    public String QUERY_PARAM = "count"; // Parameter for the search string

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setUPRV:

        // initViews:()
        initViews();

        // allows the use of building a URI:
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    /**
     * initViews():
     * this method will find all the views needed for the main activity:
     */
    public void initViews()
    {
        m_tv_NumberOfCardsRemaining = findViewById(R.id.tv_cards_remaining);
        m_btn_ShuffleNewDeck = findViewById(R.id.btn_shuffle_new_deck);
        m_et_SpecifiedCards = findViewById(R.id.et_draw_cards);
        m_btn_DrawCards = findViewById(R.id.btn_display_drawn_cards);
        m_RV_CardDeck = findViewById(R.id.rv_user_cards);


        setUpRV();
    }

    /**
     * setUpRv()
     * this method will set the RV views and automatically adapt image views into the adapter
     */
    public void setUpRV()
    {
        cardDeck = new ArrayList<>();

        int numOfColumns = 2;

        m_RV_CardDeck.setLayoutManager( new GridLayoutManager(this, numOfColumns));
        m_RV_CardDeck.setHasFixedSize(false);

        // card deck adapter:
        // set listener:
        cardDeckAdapter = new CardDeckAdapter(this, cardDeck);
        cardDeckAdapter.setClickListener((CardDeckAdapter.ItemClickListener) this);

        // set adapter on the RV:
        m_RV_CardDeck.setAdapter(cardDeckAdapter);

        // notify data set changed:
        Log.d(LOG_TAG, "setupRV called!");

        cardDeckAdapter.notifyDataSetChanged();

    }

    /**
     * connectToAPItoGetData():
     * this method will create a retrofit service and gather the jsons response:
     @param: int numOfUserSelection
     */
    private void connectToGetApiData(int numOfUserSelection)
    {
        if( retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(deckOfCards_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }

        // create your card deck service:
        CardDeckService cardDeckService = retrofit.create(CardDeckService.class);

        /* create your call back: */
        Call<SelectedCardsJsonResponse> selectedCardsJsonResponseCall = cardDeckService.getSelectedCards(shuffledDeck.getDeckId(), numOfUserSelection);
        selectedCardsJsonResponseCall.enqueue(new Callback<SelectedCardsJsonResponse>() {
            @Override
            public void onResponse(Call<SelectedCardsJsonResponse> call, Response<SelectedCardsJsonResponse> response) {

                Log.d(LOG_TAG, "onResponse: " +  response.body().toString());

                cardDeck = response.body().getCardList();

            }

            @Override
            public void onFailure(Call<SelectedCardsJsonResponse> call, Throwable t) {

            }
        });
    }

    /**
     * hideKeyboard():
     * hides the keyboard once user is typing
     */
    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * downloadURL():
     * displays connection from page
     */
    public String downloadUrl(String cardDeckUrl) throws IOException {

        InputStream inputStream = null;

        // Only dislay the first 100 characters of the
        // web page content:
        int len = 100;


        HttpURLConnection connection = null;
        try {

            URL url = new URL(cardDeckUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000 /* milliseconds */);
            connection.setConnectTimeout(15000 /* milliseconds */);

            // Start the Query:
            connection.connect();

            int response = connection.getResponseCode();

            Log.d(LOG_TAG, "The response is" + response);

            inputStream = connection.getInputStream();

            // conver the input stream into a string
            String contentAsString = convertInputToString(inputStream, len);
            return contentAsString;

            // Close the InputStream and connectoin
        } finally {

            connection.disconnect();

            if (inputStream != null) {
                inputStream.close();
            }
        } // ends try-finally for url connection
    } // ends downloadUrl()

    /**
     * convertInputToString()
     * - converts the InputStream to a string so that the activity
     *   can display it, the method uses an InputStreamReader instance to read bytes and decodes them into characters:
     */
    public String convertInputToString(InputStream stream, int length) throws IOException {

        Reader reader = null;
g
        reader = new InputStreamReader(stream, "UTF-8");

        char[] buffer = new char[length];

        reader.read(buffer);

        return new String(buffer);
    } // ends convertInputToString()

} // ends Main Activity

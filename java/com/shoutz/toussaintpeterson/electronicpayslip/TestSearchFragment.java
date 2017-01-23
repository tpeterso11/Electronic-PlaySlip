package com.shoutz.toussaintpeterson.electronicpayslip;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import models.Game;
import models.Ticket;

/**
 * Created by toussaintpeterson on 11/15/16.
 */

public class TestSearchFragment extends AbstractEPSFragment implements AdapterView.OnItemSelectedListener{
    private String title;
    private int page;
    private ArrayList<String> categories;
    private String[] languages = { "Search Filters", "C# Language", "HTML Language",
            "XML Language", "PHP Language" };
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private ExpandableListView list;
    private ArrayList<String> filter;
    private ArrayList<Integer> positons;
    private TextView filterButton;
    private LinearLayout filters;
    private ImageView closeFilters;
    private RelativeLayout searchFilters;
    private RelativeLayout clearFilters;
    private ArrayList<Game> tempGames;
    private ArrayList<Game> searchList;
    private ArrayList<Game> games;
    private CustomExpandableListView grid;
    private boolean isSearch;
    private EditText searchField;
    private TextView criteria;
    private ImageView down;
    private TextView search;
    private TextView searchTag;


    //private ImageView shopIcon;
    //@BindView(R.id.shop_layout) RelativeLayout shopLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.test_search_frag, container, false);

        list = (ExpandableListView)rootView.findViewById(R.id.numbers);
        filterButton = (TextView)rootView.findViewById(R.id.filter_button);
        filters = (LinearLayout)rootView.findViewById(R.id.filters);
        closeFilters = (ImageView)rootView.findViewById(R.id.close_filters);
        searchFilters = (RelativeLayout)rootView.findViewById(R.id.search_filter);
        clearFilters = (RelativeLayout)rootView.findViewById(R.id.clear_filter);
        grid = (CustomExpandableListView)rootView.findViewById(R.id.grid);
        searchField = (EditText)rootView.findViewById(R.id.search_field);
        criteria = (TextView)rootView.findViewById(R.id.criteria);
        down = (ImageView)rootView.findViewById(R.id.down);
        search = (TextView) rootView.findViewById(R.id.plus);
        searchTag = (TextView)rootView.findViewById(R.id.search_tag);
        isSearch = false;
        games = getEpsApplication().games;

        grid.setAdapter(new GridViewAdapter(getActivity()));

        if(searchList == null) {
            searchList = new ArrayList<Game>();
        }
        if(tempGames == null){
            tempGames = games;
        }

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(filters.getVisibility() == View.INVISIBLE) {
                    Animation out = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_bottom);
                    down.startAnimation(out);
                    down.setVisibility(View.GONE);

                    out.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right);
                            filters.startAnimation(in);
                            filters.setVisibility(View.VISIBLE);

                            AnimationSet set = new AnimationSet(true);
                            Animation slide = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_top);
                            Animation fade = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                            set.addAnimation(slide);
                            set.addAnimation(fade);

                            closeFilters.startAnimation(set);
                            closeFilters.setVisibility(View.VISIBLE);
                            closeFilters.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    closeFilter();
                                }
                            });

                            searchFilters.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ExpandableListAdapter listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
                                    ExpandableListView expListView = (ExpandableListView) rootView.findViewById(R.id.numbers);
                                    expListView.setAdapter(listAdapter);
                                    //expListView.invalidateViews();

                                    if (!searchList.isEmpty()) {
                                        searchList.clear();
                                    }

                                    expListView.collapseGroup(0);
                                    expListView.collapseGroup(1);
                                    expListView.collapseGroup(2);
                                    expListView.collapseGroup(3);
                                    expListView.collapseGroup(4);
                                    expListView.collapseGroup(5);

                                    searchWithFilters(filter);

                                    closeFilter();
                                }
                            });
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
            }
        });

        clearFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpandableListAdapter listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
                list.setAdapter(listAdapter);

                list.invalidateViews();
                filter.clear();
                searchList.clear();
            }
        });
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(filters.getVisibility() == View.INVISIBLE) {
                    Animation out = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_bottom);
                    down.startAnimation(out);
                    down.setVisibility(View.GONE);

                    out.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right);
                            filters.startAnimation(in);
                            filters.setVisibility(View.VISIBLE);

                            AnimationSet set = new AnimationSet(true);
                            Animation slide = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_top);
                            Animation fade = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                            set.addAnimation(slide);
                            set.addAnimation(fade);

                            closeFilters.startAnimation(set);
                            closeFilters.setVisibility(View.VISIBLE);
                            closeFilters.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    closeFilter();
                                }
                            });

                            searchFilters.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ExpandableListAdapter listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
                                    ExpandableListView expListView = (ExpandableListView) rootView.findViewById(R.id.numbers);
                                    expListView.setAdapter(listAdapter);
                                    //expListView.invalidateViews();

                                    if (!searchList.isEmpty()) {
                                        searchList.clear();
                                    }

                                    expListView.collapseGroup(0);
                                    expListView.collapseGroup(1);
                                    expListView.collapseGroup(2);
                                    expListView.collapseGroup(3);
                                    expListView.collapseGroup(4);
                                    expListView.collapseGroup(5);

                                    searchWithFilters(filter);

                                    closeFilter();
                                }
                            });
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
            }
        });

        filter = new ArrayList<String>();
        positons = new ArrayList<Integer>();

        setupFilters();

        ExpandableListAdapter listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
        list.setAdapter(listAdapter);

        searchField.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchGames(searchField.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                //searchGames(searchField.getText().toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

        });

        searchField.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                actionId == EditorInfo.IME_ACTION_NEXT ||
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                            hideKeyBoard(searchField);

                            if(searchList.isEmpty()){
                                isSearch = false;
                                grid.setAdapter(new GridViewAdapter(getActivity()));
                            }
                            return true;
                        }
                        return false;
                    }
                });

        setFonts();
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 4);
        title = getArguments().getString("Search");
    }


    public static TestSearchFragment newInstance() {
        TestSearchFragment fragmentFirst = new TestSearchFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", 4);
        args.putString("someTitle", "Search");
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        String item = parent.getItemAtPosition(i).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void setupFilters(){
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add(getResources().getString(R.string.play_style));
        listDataHeader.add(getResources().getString(R.string.odds));
        listDataHeader.add(getResources().getString(R.string.theme));
        listDataHeader.add(getResources().getString(R.string.top_prize));
        listDataHeader.add(getResources().getString(R.string.draw_freq));
        listDataHeader.add(getResources().getString(R.string.price));

        ArrayList<String> playList = new ArrayList<String>();
        playList.addAll(Arrays.asList(getResources().getStringArray(R.array.play_styles)));
        listDataChild.put(listDataHeader.get(0), playList);
        ArrayList<String> oddsList = new ArrayList<String>();
        oddsList.addAll(Arrays.asList(getResources().getStringArray(R.array.odds)));
        listDataChild.put(listDataHeader.get(1), oddsList);
        ArrayList<String> themeList = new ArrayList<String>();
        themeList.addAll(Arrays.asList(getResources().getStringArray(R.array.theme)));
        listDataChild.put(listDataHeader.get(2), themeList);
        ArrayList<String> prizeList = new ArrayList<String>();
        prizeList.addAll(Arrays.asList(getResources().getStringArray(R.array.top_prize)));
        listDataChild.put(listDataHeader.get(3), prizeList);
        ArrayList<String> drawList = new ArrayList<String>();
        drawList.addAll(Arrays.asList(getResources().getStringArray(R.array.frequency)));
        listDataChild.put(listDataHeader.get(4), drawList);
        ArrayList<String> priceList = new ArrayList<String>();
        priceList.addAll(Arrays.asList(getResources().getStringArray(R.array.price)));
        listDataChild.put(listDataHeader.get(5), priceList);
    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private List<String> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<String, List<String>> _listDataChild;
        //private CheckBox check;

        public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                     HashMap<String, List<String>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            String childText = (String) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.expandable_sub_list, null);
            }

            final TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.lblListItem);

            txtListChild.setText("");
            //txtListChild.setTypeface(sub);

            txtListChild.setText(childText);
            txtListChild.setTypeface(getEpsApplication().main);

            if(filter.contains(childText) && txtListChild.getText().toString().equals(childText)){
                ImageView check = (ImageView)convertView.findViewById(R.id.check);
                check.setVisibility(View.VISIBLE);
            }
            else{
                ImageView check = (ImageView)convertView.findViewById(R.id.check);
                check.setVisibility(View.GONE);
            }

            //CheckBox check = (CheckBox) convertView.findViewById(R.id.check);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //CheckBox check = (CheckBox) view.findViewById(R.id.check);
                    String param = txtListChild.getText().toString();
                    if(filter.contains(param)){
                        int position = filter.indexOf(param);
                        filter.remove(position);
                        ImageView check = (ImageView)view.findViewById(R.id.check);
                        check.setVisibility(View.GONE);
                        //check.setChecked(false);
                    }
                    else {
                        //check.setTag(childPosition);
                        //check.setChecked(true);
                        ImageView check = (ImageView)view.findViewById(R.id.check);
                        check.setVisibility(View.VISIBLE);
                        filter.add(txtListChild.getText().toString());
                        //Toast.makeText(getApplicationContext(), txtListChild.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String headerTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.expandable_list_filter, null);
            }

            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.cell_title);
            lblListHeader.setText(headerTitle);
            lblListHeader.setTypeface(getEpsApplication().sub);


            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    private void closeFilter(){
        Animation out = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_right);
        filters.startAnimation(out);
        filters.setVisibility(View.INVISIBLE);

        AnimationSet set = new AnimationSet(true);
        Animation slide = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_top);
        Animation fade = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
        set.addAnimation(slide);
        set.addAnimation(fade);

        closeFilters.startAnimation(set);
        closeFilters.setVisibility(View.GONE);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_top);
                down.startAnimation(in);
                down.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void searchWithFilters(ArrayList<String> params){
        for(String string : params){
            for(Game game : tempGames) {
                if (game.getFilterString().contains(string) && !(searchList.contains(game))) {
                    searchList.add(game);
                }
            }
        }

        isSearch = true;

        grid.setAdapter(new GridViewAdapter(getActivity()));
    }

    public class GridViewAdapter extends BaseAdapter {

        private Context mContext;

        public GridViewAdapter(Context mContext) {
            this.mContext = mContext;
        }


        @Override
        public int getCount() {
            if(isSearch){
                return searchList.size();
            }
            else {
                return games.size();
            }
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View gridView;

            if (convertView == null) {

                gridView = new View(getActivity());

                // get layout from mobile.xml
                gridView = inflater.inflate(R.layout.search_row, null);

                // set value into textview
                TextView textView = (TextView) gridView
                        .findViewById(R.id.game_name);
                textView.setTypeface(getEpsApplication().sub);

                ImageView tile = (ImageView)gridView.findViewById(R.id.tile);

                if(isSearch) {
                    textView.setText(searchList.get(position).getGameName());
                    Glide.with(getActivity())
                            .load(searchList.get(position).getIconUrl())
                            .crossFade(R.anim.fade_in, 800)
                            .into(tile);

                    for(Game game : getEpsApplication().games){
                        if(searchList.get(position).getGameName().equals(game.getGameName())){
                            int number = Integer.valueOf(game.getTopPrize());
                            DecimalFormat formatter = new DecimalFormat("#,###,###");
                            String formattedNum = formatter.format(number);

                            String value = "$" + formattedNum;
                            TextView topPrize = (TextView)gridView.findViewById(R.id.top_prize);
                            topPrize.setText(value);
                            break;
                        }
                    }
                }
                else{
                    textView.setText(games.get(position).getGameName());
                    Glide.with(getActivity())
                            .load(games.get(position).getIconUrl())
                            .crossFade(R.anim.fade_in, 800)
                            .into(tile);

                    for(Game game : getEpsApplication().games){
                        if(games.get(position).getGameName().equals(game.getGameName())){
                            int number = Integer.valueOf(game.getTopPrize());
                            DecimalFormat formatter = new DecimalFormat("#,###,###");
                            String formattedNum = formatter.format(number);

                            String value = "$" + formattedNum;
                            TextView topPrize = (TextView)gridView.findViewById(R.id.top_prize);
                            topPrize.setText(value);
                            break;
                        }
                    }

                    gridView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(isSearch) {
                                ((LandingActivity) getActivity()).launchGame(createBundle(searchList.get(position).getGameName(), searchList.get(position).getMaxValue(), searchList.get(position).getGameId(), searchList.get(position).getIconSet(), searchList.get(position).getGameDescription(), searchList.get(position).getExtendedDescription(), searchList.get(position).getMaxNumber(), searchList.get(position).getWager(), searchList.get(position).getBannerUrl(),  searchList.get(position).getTopPrize()));
                            }
                            else{
                                ((LandingActivity) getActivity()).launchGame(createBundle(games.get(position).getGameName(), games.get(position).getMaxValue(), games.get(position).getGameId(), games.get(position).getIconSet(), games.get(position).getGameDescription(), games.get(position).getExtendedDescription(), games.get(position).getMaxNumber(), games.get(position).getWager(), games.get(position).getBannerUrl(),  games.get(position).getTopPrize()));
                            }
                        }
                    });

                }

            } else {
                gridView = (View) convertView;
            }

            return gridView;
        }
    }

    private void searchGames(String searchParam){
        isSearch = true;
        searchList.clear();

        for (Game game : tempGames) {
            String searchName = game.getGameName();
            String searchNameLower = game.getGameName().toLowerCase();
            if (!(searchParam.equals("")) && searchName.equalsIgnoreCase(searchParam.trim()) || !(searchParam.equals("")) && (searchName.contains(searchParam.trim()))
                    || !(searchParam.equals("")) && (searchNameLower.equalsIgnoreCase(searchParam)) || !(searchParam.equals("")) && (searchNameLower.contains(searchParam))) {
                searchList.add(game);
            }
        }

        grid.setAdapter(new GridViewAdapter(getActivity()));

        if (criteria != null && searchList.isEmpty() && !(searchParam.equals(""))) {
            criteria.setVisibility(View.VISIBLE);
        } else if (searchList.isEmpty() && !(searchParam.equals(""))) {
            criteria.setText("");
        } else {
            criteria.setVisibility(View.GONE);
        }
    }

    private void hideKeyBoard(EditText target){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(target.getWindowToken(), 0);
    }

    private void setFonts(){
        search.setTypeface(getEpsApplication().sub);
        filterButton.setTypeface(getEpsApplication().sub);
        searchTag.setTypeface(getEpsApplication().sub);
    }

    private Bundle createBundle(String gamename, String maxValue, String gameId, String iconSet, String description, String extendedDescription, String maxChoice, String cost, String tileUrl, String topPrize) {
        Bundle args = new Bundle();
        args.putString("gameName", gamename);
        args.putString("gameId", gameId);
        args.putString("topPrize", topPrize);
        args.putString("maxValue", maxValue);
        args.putString("description", description);
        args.putString("extendedDescription", extendedDescription);
        args.putString("iconSet", iconSet);
        args.putString("maxChoice", maxChoice);
        args.putString("cost", cost);
        args.putString("tileUrl", tileUrl);
        args.putInt("someInt", 2);
        args.putString("someTitle", "Game");

        return args;
    }
}


package za.co.weather.nav;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import java.util.List;

import za.co.weather.MainActivity;
import za.co.weather.R;
import za.co.weather.objs.Position;
import za.co.weather.utils.FragmentUtils;
import za.co.weather.utils.GeneralUtils;
import za.co.weather.utils.SQLiteUtils;

public class FavouritesFrag extends Fragment
{
    private ListView lvFavourites;
    private List<Position> positions;
    private FavouritesAdapter favouritesAdapter;

    private SQLiteUtils sqLiteUtils;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_favourites, container, false);

        this.sqLiteUtils = new SQLiteUtils(getContext());

        wireUI(view);

        return view;
    }

    private void wireUI(View view)
    {
        this.lvFavourites = (ListView) view.findViewById(R.id.lvFavourties);

        List<Position> positions = this.sqLiteUtils.getAllFavourites();
        if(positions != null)
        {
            setFavouritesAdapter(positions);
            setLvFavourites(this.favouritesAdapter);
        }

        addLvFavouritesListener();

    }

    private void addLvFavouritesListener()
    {
        this.lvFavourites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int index, long id) {

                Position position = (Position) adapter.getItemAtPosition(index);
                JSONObject jsonObjectPosition = position.toJSON();

                if (jsonObjectPosition != null)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("position", jsonObjectPosition.toString());

                    HomeFrag homeFrag = new HomeFrag();
                    homeFrag.setArguments(bundle);

                    FragmentUtils.startFragment(((MainActivity)getActivity()).getSupportFragmentManager(), homeFrag, R.id.fragContainer, ((MainActivity)getActivity()).getSupportActionBar(), "Home", true, false, true, null);
                }else
                {
                    GeneralUtils.makeToast(getContext(), "Favourite location not available!");
                }
            }
        });
    }


    private class FavouritesAdapter extends BaseAdapter
    {
        private List<Position> positions;

        private TextView txtCity;

        public FavouritesAdapter(List<Position> positions)
        {
            this.positions = positions;
        }

        @Override
        public int getCount() {

            int toReturn = 0;

            if(this.positions != null)
            {
                toReturn = this.positions.size();
            }

            return toReturn;
        }

        @Override
        public Position getItem(int index) {

            Position toReturn = null;

            if(this.positions != null )
            {
                toReturn = this.positions.get(index);
            }

            return toReturn;

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null)
            {
                convertView = getLayoutInflater().inflate(R.layout.favourites_list_item, null);
            }

            wireUI(convertView, position);

            return convertView;
        }

        private void wireUI(View view, final int index)
        {
            this.txtCity = (TextView) view.findViewById(R.id.txtCity);
            this.txtCity.setText(this.positions.get(index).getCity());
        }
    }


    public void setFavouritesAdapter(List<Position> positions) {
        this.favouritesAdapter = new FavouritesAdapter(positions);
    }

    public void setLvFavourites(FavouritesAdapter favouritesAdapter) {
        this.lvFavourites.setAdapter(favouritesAdapter);
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    public FavouritesAdapter getFavouritesAdapter() {
        return favouritesAdapter;
    }

    public ListView getLvFavourites() {
        return lvFavourites;
    }

}

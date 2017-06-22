package com.micste.friendlyweather;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.WeatherViewHolder> {

    public static class WeatherViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView place;
        TextView temp;
        TextView windSpeed;

        WeatherViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            place = (TextView) itemView.findViewById(R.id.weather_place);
            temp = (TextView) itemView.findViewById(R.id.weather_temp);
            windSpeed = (TextView) itemView.findViewById(R.id.weather_windspeed);
        }
    }

    List<Weather> weathers;

    public RvAdapter(List<Weather> weathers) {
        this.weathers = weathers;
    }

    @Override
    public int getItemCount() {
        return weathers.size();
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_weather, parent, false);
        WeatherViewHolder viewHolder = new WeatherViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder viewHolder, int position) {
        viewHolder.place.setText(weathers.get(position).getPlace());
        viewHolder.temp.setText(weathers.get(position).getTemperature());
        viewHolder.windSpeed.setText(weathers.get(position).getWindSpeed());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}

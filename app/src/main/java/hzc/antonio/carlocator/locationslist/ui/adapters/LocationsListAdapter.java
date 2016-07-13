package hzc.antonio.carlocator.locationslist.ui.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import hzc.antonio.carlocator.R;
import hzc.antonio.carlocator.domain.Util;
import hzc.antonio.carlocator.entities.CarLocation;
import hzc.antonio.carlocator.entities.CustomAddress;
import hzc.antonio.carlocator.libs.base.ImageLoader;

public class LocationsListAdapter extends RecyclerView.Adapter<LocationsListAdapter.ViewHolder> {
    private Context context;
    private ImageLoader imageLoader;
    private List<CarLocation> dataset;
    private OnItemClickListener onItemClickListener;

    public LocationsListAdapter(Context context, ImageLoader imageLoader, List<CarLocation> dataset, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.imageLoader = imageLoader;
        this.dataset = dataset;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_locations_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CarLocation carLocation = dataset.get(position);
        CustomAddress address = carLocation.getAddress();

        holder.setOnItemClickListener(carLocation, onItemClickListener);

        holder.lblCity.setText(address.getCity());
        holder.lblZip.setText("(" + address.getPostalCode() + " " + address.getProvince() + ")");
        holder.lblStreet.setText(address.getStreet());
        holder.lblDate.setText(carLocation.getTimestamp());

        imageLoader.load(holder.imgMap, Util.getImageMapUrl(carLocation, false));

        int currentColor = ContextCompat.getColor(context, R.color.colorAccent);
        int notCurrentColor = ContextCompat.getColor(context, R.color.colorSecondaryText);
        if (carLocation.isCurrent()) {
            holder.btnLaunchNavigation.setVisibility(View.VISIBLE);
            holder.btnRemove.setVisibility(View.GONE);
            holder.btnMakeCurrent.setVisibility(View.GONE);

            holder.btnShare.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_share_24dp_accent));
            holder.btnDisplayStreetView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_streetview_24dp_accent));

            holder.lblCity.setTextColor(currentColor);
            holder.lblZip.setTextColor(currentColor);
            holder.lblStreet.setTextColor(currentColor);
            holder.lblDate.setTextColor(currentColor);
        }
        else {
            holder.btnLaunchNavigation.setVisibility(View.GONE);
            holder.btnRemove.setVisibility(View.VISIBLE);
            holder.btnMakeCurrent.setVisibility(View.VISIBLE);

            holder.btnShare.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_share_24dp_primary));
            holder.btnDisplayStreetView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_streetview_24dp_primary));

            holder.lblCity.setTextColor(notCurrentColor);
            holder.lblZip.setTextColor(notCurrentColor);
            holder.lblStreet.setTextColor(notCurrentColor);
            holder.lblDate.setTextColor(notCurrentColor);
        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void setCarLocations(List<CarLocation> list) {
        dataset = list;
        notifyDataSetChanged();
    }

    public void removeCarLocation(CarLocation carLocation) {
        dataset.remove(carLocation);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgMap) CircleImageView imgMap;
        @BindView(R.id.lblStreet) TextView lblStreet;
        @BindView(R.id.lblCity) TextView lblCity;
        @BindView(R.id.lblZip) TextView lblZip;
        @BindView(R.id.lblDate) TextView lblDate;
        @BindView(R.id.btnShare) ImageButton btnShare;
        @BindView(R.id.btnDisplayStreetView) ImageButton btnDisplayStreetView;
        @BindView(R.id.btnLaunchNavigation) ImageButton btnLaunchNavigation;
        @BindView(R.id.btnRemove) ImageButton btnRemove;
        @BindView(R.id.btnMakeCurrent) ImageButton btnMakeCurrent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setOnItemClickListener(final CarLocation carLocation, final OnItemClickListener listener) {
            btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onShareClick(carLocation);
                }
            });

            btnDisplayStreetView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDisplayStreetViewClick(carLocation);
                }
            });

            btnLaunchNavigation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onLaunchNavigationClick(carLocation);
                }
            });

            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onRemoveClick(carLocation);
                }
            });

            btnMakeCurrent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMakeCurrentClick(carLocation);
                }
            });

            imgMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onImageMapClick(carLocation);
                }
            });
        }
    }
}

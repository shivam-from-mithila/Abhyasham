package in.practix.app;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HeaderAdapter
        extends RecyclerView.Adapter<HeaderAdapter.HeaderViewHolder> {


    @NonNull
    @Override
    public HeaderViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                R.layout.item_home_header,
                                parent,
                                false
                        );

        return new HeaderViewHolder(view);
    }


    @Override
    public void onBindViewHolder(
            @NonNull HeaderViewHolder holder,
            int position) {

        // =========================================
        // LOCAL PRACTICE STATISTICS
        // =========================================

        int practicedQuestions =
                PracticeStatsManager
                        .getPracticedQuestions(
                                holder.itemView.getContext()
                        );


        int accuracy =
                PracticeStatsManager
                        .getAccuracy(
                                holder.itemView.getContext()
                        );


        int dayStreak =
                PracticeStatsManager
                        .getDayStreak(
                                holder.itemView.getContext()
                        );


        // =========================================
        // SHOW STATISTICS
        // =========================================

        holder.questionsValue.setText(
                String.valueOf(
                        practicedQuestions
                )
        );


        holder.accuracyValue.setText(
                accuracy + "%"
        );


        holder.streakValue.setText(
                String.valueOf(
                        dayStreak
                )
        );
    }


    @Override
    public int getItemCount() {

        return 1;
    }


    static class HeaderViewHolder
            extends RecyclerView.ViewHolder {

        TextView namaskaram;
        TextView abhyasham;

        TextView questionsValue;
        TextView accuracyValue;
        TextView streakValue;


        HeaderViewHolder(
                @NonNull View itemView) {

            super(itemView);


            // =====================================
            // Greeting
            // =====================================

            namaskaram =
                    itemView.findViewById(
                            R.id.greeting1
                    );


            abhyasham =
                    itemView.findViewById(
                            R.id.greeting2
                    );


            // =====================================
            // Statistics
            // =====================================

            questionsValue =
                    itemView.findViewById(
                            R.id.questionsValue
                    );


            accuracyValue =
                    itemView.findViewById(
                            R.id.accuracyValue
                    );


            streakValue =
                    itemView.findViewById(
                            R.id.streakValue
                    );



        }

    }
}
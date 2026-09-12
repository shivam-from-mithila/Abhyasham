package in.practix.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    private final List<Topic> topicList;
    private final OnTopicSelectionChanged listener;


    // Interface
    public interface OnTopicSelectionChanged {
        void onSelectionChanged(int totalQuestions);
    }


    // Constructor
    public TopicAdapter(
            List<Topic> topicList,
            OnTopicSelectionChanged listener) {

        this.topicList = topicList;
        this.listener = listener;
    }


    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_topic, parent, false);

        return new TopicViewHolder(view);
    }


    @Override
    public void onBindViewHolder(
            @NonNull TopicViewHolder holder,
            int position) {

        Topic topic = topicList.get(position);

        holder.tvTopicName.setText(topic.getName());

        holder.tvQuestionCount.setText(
                topic.getQuestionCount() + " Questions"
        );


        // Current selection state
        holder.checkTopic.setChecked(topic.isSelected());

        if (topic.isSelected()) {

            holder.cardTopic.setStrokeColor(
                    androidx.core.content.ContextCompat.getColor(
                            holder.itemView.getContext(),
                            R.color.topicCardSelectedStroke
                    )
            );

            holder.cardTopic.setStrokeWidth(2);
            holder.cardTopic.setCardBackgroundColor(
                    androidx.core.content.ContextCompat.getColor(
                            holder.itemView.getContext(),
                            R.color.topicCardSelectedBg
                    )
            );

        } else {

            holder.cardTopic.setStrokeColor(
                    androidx.core.content.ContextCompat.getColor(
                            holder.itemView.getContext(),
                            R.color.topicCardStroke
                    )
            );

            holder.cardTopic.setStrokeWidth(1);

            holder.cardTopic.setCardBackgroundColor(
                    androidx.core.content.ContextCompat.getColor(
                            holder.itemView.getContext(),
                            R.color.cardBgColor
                    )
            );
        }


        // Card click
        holder.cardTopic.setOnClickListener(v -> {

            boolean newState = !topic.isSelected();

            // Update model
            topic.setSelected(newState);

            // Update checkbox
            holder.checkTopic.setChecked(newState);


            // Update card border
            if (newState) {

                holder.cardTopic.setStrokeColor(
                        androidx.core.content.ContextCompat.getColor(
                                holder.itemView.getContext(),
                                R.color.topicCardSelectedStroke
                        )
                );

                holder.cardTopic.setStrokeWidth(2);

            } else {

                holder.cardTopic.setStrokeColor(
                        androidx.core.content.ContextCompat.getColor(
                                holder.itemView.getContext(),
                                R.color.topicCardStroke
                        )
                );

                holder.cardTopic.setStrokeWidth(1);
            }


            // Activity ko updated question count bhejo
            listener.onSelectionChanged(
                    getSelectedQuestionCount()
            );
        });
    }


    @Override
    public int getItemCount() {
        return topicList.size();
    }


    // Get selected topics
    public List<Topic> getSelectedTopics() {

        List<Topic> selectedTopics = new ArrayList<>();

        for (Topic topic : topicList) {

            if (topic.isSelected()) {
                selectedTopics.add(topic);
            }
        }

        return selectedTopics;
    }


    // Get total questions of selected topics
    public int getSelectedQuestionCount() {

        int total = 0;

        for (Topic topic : topicList) {

            if (topic.isSelected()) {
                total += topic.getQuestionCount();
            }
        }

        return total;
    }


    // Get total questions of all topics
    public int getTotalQuestionCount() {

        int total = 0;

        for (Topic topic : topicList) {
            total += topic.getQuestionCount();
        }

        return total;
    }


    // ViewHolder
    static class TopicViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView cardTopic;
        TextView tvTopicName;
        TextView tvQuestionCount;
        MaterialCheckBox checkTopic;


        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);

            cardTopic = itemView.findViewById(R.id.cardTopic);

            tvTopicName = itemView.findViewById(R.id.tvTopicName);

            tvQuestionCount = itemView.findViewById(
                    R.id.tvQuestionCount
            );

            checkTopic = itemView.findViewById(
                    R.id.checkTopic
            );
        }
    }
}
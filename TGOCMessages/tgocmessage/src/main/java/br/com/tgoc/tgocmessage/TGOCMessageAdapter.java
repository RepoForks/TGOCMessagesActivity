package br.com.tgoc.tgocmessage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

/**
 * Created by rodrigocavalcante on 8/31/16.
 */
public class TGOCMessageAdapter extends RecyclerView.Adapter<TGOCMessageViewHolder> {

    TGOCMessageActivityInterface tgocMessageActivityInterface;

    public TGOCMessageAdapter(TGOCMessageActivityInterface tgocMessageInterface) {
        this.tgocMessageActivityInterface = tgocMessageInterface;
    }

    @Override
    public TGOCMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(tgocMessageActivityInterface.messageBubbleAtPosition(viewType).getLayoutResource(), parent, false);

        return new TGOCMessageViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return tgocMessageActivityInterface.numberOfItemsInConversation();
    }

    @Override
    public void onBindViewHolder(TGOCMessageViewHolder view, int position) {

        TGOCMessageInterface tgocMessageInterface = tgocMessageActivityInterface.messageDataAtPosition(position);
        if(tgocMessageInterface != null) {
            view.tgoc_message_text.setText(tgocMessageInterface.getText());
        }

        if(tgocMessageInterface.getSenderDisplayName() != null) {
            view.tgoc_sender_display_name.setVisibility(View.VISIBLE);
            view.tgoc_sender_display_name.setText(tgocMessageInterface.getSenderDisplayName());
        } else
            view.tgoc_sender_display_name.setVisibility(View.GONE);

        TGOCAvatarInterface tgocAvatarInterface = tgocMessageActivityInterface.avatarAtPosition(position);
        if(tgocAvatarInterface != null) {
            view.tgoc_avatar.setVisibility(View.VISIBLE);
            Glide.with(view.mView.getContext()).load(tgocAvatarInterface.getData()).into(view.tgoc_avatar);
        } else
            view.tgoc_avatar.setVisibility(View.GONE);

        TGOCBubbleInterface tgocBubbleInterface = tgocMessageActivityInterface.messageBubbleAtPosition(position);
        view.tgoc_bubble_layout.setBackground(tgocBubbleInterface.getDrawable());

        tgocMessageActivityInterface.bindViewHolderAtPosition(view, position);
    }
}

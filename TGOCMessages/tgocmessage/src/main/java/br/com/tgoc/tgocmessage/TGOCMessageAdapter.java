package br.com.tgoc.tgocmessage;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by rodrigocavalcante on 8/31/16.
 */
public class TGOCMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPING = -1;

    TGOCMessageActivityInterface tgocMessageActivityInterface;
    TGOCMessageActivityTypingInterface tgocMessageActivityTypingInterface;

    public TGOCMessageAdapter(TGOCMessageActivityInterface tgocMessageInterface, TGOCMessageActivityTypingInterface tgocMessageActivityTypingInterface) {
        this.tgocMessageActivityInterface = tgocMessageInterface;
        this.tgocMessageActivityTypingInterface = tgocMessageActivityTypingInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPING)
            return new TGOCTypingViewHolder(LayoutInflater.from(parent.getContext()).inflate(tgocMessageActivityTypingInterface.typingBubble().getLayoutResource(), parent, false));
        else
            return new TGOCMessageBubbleViewHolder(LayoutInflater.from(parent.getContext()).inflate(tgocMessageActivityInterface.messageBubbleAtPosition(viewType).getLayoutResource(), parent, false));
    }

    @Override
    public int getItemViewType(int position) {

        if (position >= tgocMessageActivityInterface.numberOfItemsInConversation())
            return TYPING;

        return position;
    }

    @Override
    public int getItemCount() {

        if (tgocMessageActivityTypingInterface.isTyping())
            return tgocMessageActivityInterface.numberOfItemsInConversation() + 1;

        return tgocMessageActivityInterface.numberOfItemsInConversation();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view, int position) {
        if (position >= tgocMessageActivityInterface.numberOfItemsInConversation()) {
            TGOCTypingViewHolder holder = (TGOCTypingViewHolder) view;

            holder.getTextView().setText(tgocMessageActivityTypingInterface.typingText());
            TGOCBubbleInterface tgocBubbleInterface = tgocMessageActivityTypingInterface.typingBubble();
            holder.tgoc_bubble_layout.getBackground().setColorFilter(tgocBubbleInterface.getColorFilter());

            TGOCAvatarInterface tgocAvatarInterface = tgocMessageActivityTypingInterface.typingAvatar();
            if (tgocAvatarInterface != null) {
                holder.getAvatar().setVisibility(View.VISIBLE);
                tgocAvatarInterface.bindImageView(holder.tgoc_avatar);
            } else
                holder.getAvatar().setVisibility(View.GONE);

        } else {
            TGOCMessageBubbleViewHolder holder = (TGOCMessageBubbleViewHolder) view;

            TGOCMessageInterface tgocMessageInterface = tgocMessageActivityInterface.messageDataAtPosition(position);
            bindMessageBubble(holder, position, tgocMessageInterface);
            tgocMessageActivityInterface.bindViewHolderAtPosition(holder, position);
        }
    }

    public void bindMessageBubble(final TGOCMessageBubbleViewHolder view, int position, final TGOCMessageInterface tgocMessageInterface) {
        TGOCBubbleInterface tgocBubbleInterface = tgocMessageActivityInterface.messageBubbleAtPosition(position);
        view.tgoc_bubble_layout.getBackground().setColorFilter(tgocBubbleInterface.getColorFilter());

        if (tgocMessageInterface != null) {
            view.getTextView().setText(tgocMessageInterface.getText());
            Linkify.addLinks(view.getTextView(), Linkify.ALL);

            if (tgocMessageInterface.getDate() != null) {
                view.getTimeTextView().setVisibility(View.VISIBLE);
                view.getTimeTextView().setText(DateFormat.getTimeFormat(view.mView.getContext()).format(tgocMessageInterface.getDate()));
            } else
                view.getTimeTextView().setVisibility(View.GONE);
        }

        if (tgocMessageInterface.getSenderDisplayName() != null) {
            view.getSenderTextView().setVisibility(View.VISIBLE);
            view.getSenderTextView().setText(tgocMessageInterface.getSenderDisplayName());
        } else
            view.getSenderTextView().setVisibility(View.GONE);

        TGOCAvatarInterface tgocAvatarInterface = tgocMessageActivityInterface.avatarAtPosition(position);
        if (tgocAvatarInterface != null) {
            view.getAvatar().setVisibility(View.VISIBLE);
            tgocAvatarInterface.bindImageView(view.getAvatar());
        } else
            view.getAvatar().setVisibility(View.GONE);

        if (tgocMessageInterface.isMediaMessage()) {
            view.tgoc_content.removeAllViews();

            View content = tgocMessageInterface.getMedia().getView(view.mView.getContext());

            view.tgoc_content.addView(content);

            content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tgocMessageActivityInterface.didSelectMessage(tgocMessageInterface);
                }
            });
        } else {
            view.getTextView().setOnClickListener(null);
        }
    }
}
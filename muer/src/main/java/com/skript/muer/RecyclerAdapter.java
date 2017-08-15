package com.skript.muer;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jon on 2/13/17.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MacroViewHolder> {

    ArrayList<String> macros;

    public RecyclerAdapter(ArrayList<String> macroList, Context context) {
        macros = macroList;
    }

    @Override
    public void onBindViewHolder(MacroViewHolder holder, int position) {
        holder.text.setText(macros.get(position).toString());

    }

    @Override
    public int getItemViewType(int position) {
        return macros.get(position).length();
    }

    @Override
    public MacroViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.macroview,parent,false);

        ((TextView)v.findViewById(R.id.macroResource)).setText("TestString");
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String command = ((TextView)view.findViewById(R.id.macroResource)).getText().toString();
                GameWindow.writeToOutput(command);
            }
        });

        ((Button)v.findViewById(R.id.removemacrobutton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("hit" + ((TextView)v.findViewById(R.id.macroResource)).getText().toString());
                macros.remove(((TextView)v.findViewById(R.id.macroResource)).getText().toString());
                for (String macro: macros) {
                    System.out.println(macro);
                }
                GameWindow.setMacros(macros);
                notifyDataSetChanged();
            }
        });
        MacroViewHolder macV = new MacroViewHolder(v);

        return macV;
    }


    @Override
    public int getItemCount() {
        return macros.size();
    }

    public static class MacroViewHolder extends RecyclerView.ViewHolder{

        //protected ImageView image;
        protected TextView text;

        public MacroViewHolder(View itemView) {
            super(itemView);
            //image= (ImageView) itemView.findViewById(R.id.image_id);
            text= (TextView) itemView.findViewById(R.id.macroResource);
        }
    }

}
package com.skript.muer;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jon on 2/13/17.
 */
public class ServerRecyclerAdapter extends RecyclerView.Adapter<ServerRecyclerAdapter.ServerViewHolder> {

    List<Server> servers;

    public ServerRecyclerAdapter(List<Server> macroList, Context context) {
        servers = macroList;
    }

    @Override
    public void onBindViewHolder(ServerViewHolder holder, int position) {
        holder.name.setText(servers.get(position).getName());
        holder.port.setText(servers.get(position).getPort() + "");
        holder.server.setText(servers.get(position).getURL());

        final String name = servers.get(position).getName();
        final int port = servers.get(position).getPort();
        final String server = servers.get(position).getURL();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                                   LoginActivity.connection[0] = ((TextView)view.findViewById(R.id.hostName)).getText().toString();
                                                   LoginActivity.connection[1] = ((TextView)view.findViewById(R.id.portNumber)).getText().toString();
                                               }
                                           }
        );

        holder.removeServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                servers.remove(new Server(port,server,"",name));
                LoginActivity.writeServers();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return servers.get(position).getURL().length();
    }

    @Override
    public ServerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.server_connection_cell,parent,false);



        ServerViewHolder macV = new ServerViewHolder(v);
        if( macV == null  || v == null) {
            System.out.println("Fail");
        }
        return macV;
    }


    @Override
    public int getItemCount() {
        return servers.size();
    }

    public static class ServerViewHolder extends RecyclerView.ViewHolder{

        //protected ImageView image;
        protected TextView server;
        protected TextView name;
        protected TextView port;
        protected Button removeServer;

        public ServerViewHolder(View itemView) {
            super(itemView);
            //image= (ImageView) itemView.findViewById(R.id.image_id);
            server= (TextView) itemView.findViewById(R.id.hostName);
            name = (TextView) itemView.findViewById(R.id.gameName);
            port = (TextView) itemView.findViewById(R.id.portNumber);
            removeServer = (Button) itemView.findViewById(R.id.removeServerButton);
        }
    }

}
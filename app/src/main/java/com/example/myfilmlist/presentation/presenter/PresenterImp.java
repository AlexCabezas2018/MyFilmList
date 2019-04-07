package com.example.myfilmlist.presentation.presenter;

import com.example.myfilmlist.exceptions.ASException;
import com.example.myfilmlist.presentation.command.Command;
import com.example.myfilmlist.presentation.command.CommandDispatcher;
import com.example.myfilmlist.presentation.context.Context;

public class PresenterImp extends Presenter {
    @Override
    public void action(Context context) throws ASException {
        Command commandToExecute = CommandDispatcher.dispatchCommand(context); //We parse the command based on the event.
        Context resultData = commandToExecute.execute(context); //We execute the parsed command, obtaining the data from the business layer.
        context.getActivity().update(resultData); //We update the view with the given data
    }
}

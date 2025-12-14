package io.github.acaimingus.worqspace.ui.collections;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CollectionsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CollectionsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the collections fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
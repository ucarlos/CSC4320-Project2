package com.example.csc4320_project_2.ui.browsefs;

/*
 * -----------------------------------------------------------------------------
 * Created by Ulysses Carlos on 11/25/2020 at 10:33 PM
 *
 * FileSystemAdapterContainer.java
 * Contains a reference to a FileSystemAdapter and makes sure that its items
 * are recreated when the user switches directories.
 * -----------------------------------------------------------------------------
 */



public class FileSystemAdapterContainer {
    private static FileSystemAdapter fileSystemAdapter;

    public FileSystemAdapterContainer(FileSystemAdapter adapter){
        fileSystemAdapter = adapter;
    }

    static class FileSystemAdapterRunnable implements Runnable {
        public void run(){
            // While user does not change directory: sleep

            // User has changed directory, so wake up
            // Remove all items from list corresponding to old directory
            // Add all items from list corresponding to new directory
            // Set variable back to false and sleep.
            try {
                repopulate_items();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        public synchronized void repopulate_items() throws InterruptedException {
            // Do nothing util needed.
            while (!fileSystemAdapter.directory_is_initial())
                wait();

            // Now notify that the items have been removed.
            fileSystemAdapter.notifyItemRangeRemoved(0, fileSystemAdapter.getItemCount());
            fileSystemAdapter.set_directory_status(FileSystemAdapter.Directory_Status.REGENERATING_NEW_DATASET);

            // Wake up the Adapter
            notifyAll();

            while (fileSystemAdapter.get_directory_status() == FileSystemAdapter.Directory_Status.REGENERATING_NEW_DATASET)
                wait();

            // Now notify that new items have been added to the adapter.
            fileSystemAdapter.notifyItemRangeInserted(0, fileSystemAdapter.getItemCount());
            fileSystemAdapter.set_directory_status(FileSystemAdapter.Directory_Status.INITIAL);

            // Now wake up the Adapter again.
            notifyAll();
            // Done!


        }

    }
}

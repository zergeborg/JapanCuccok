/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.japancuccok.common.infrastructure.gae;

import com.japancuccok.common.infrastructure.gaeframework.FileStorage;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.Collection;
import java.util.Vector;
import java.util.logging.Logger;

/**
 * Keeps track of files awaiting deletion, and deletes them when an associated marker object is
 * reclaimed by the garbage collector.
 * 
 * @author Noel Bergman
 * @author Martin Cooper
 */
class FileCleaner
{
    transient private static final Logger logger = Logger.getLogger(FileCleaner.class.getName());
	/**
	 * Queue of <code>Tracker</code> instances being watched.
	 */
	private static final ReferenceQueue<Object> /* Tracker */q = new ReferenceQueue<Object>();

	/**
	 * Collection of <code>Tracker</code> instances in existence.
	 */
	private static Collection<Tracker> /* Tracker */trackers = new Vector<Tracker>();

	static void reapFiles(int max) {
	    logger.fine("Reaping files...");
	    int total = 0;
	    Tracker tracker = null;
        while ((tracker = (Tracker)q.poll()) != null && total < max) {
            tracker.delete();
            tracker.clear();
            trackers.remove(tracker);
            total++;
        }
        logger.fine("Total reaped files is " + total);
	}

	/**
	 * Track the specified file, using the provided marker, deleting the file when the marker
	 * instance is garbage collected.
	 * 
	 * @param path
	 *            The full path to the file to be tracked.
	 * @param marker
	 *            The marker object used to track the file.
	 */
	public static void track(String path, Object marker)
	{
		trackers.add(new Tracker(path, marker, q));
	}

	/**
	 * Retrieve the number of files currently being tracked, and therefore awaiting deletion.
	 * 
	 * @return the number of files being tracked.
	 */
	public static int getTrackCount()
	{
		return trackers.size();
	}

	/**
	 * Inner class which acts as the reference for a file pending deletion.
	 */
	private static class Tracker extends PhantomReference<Object>
	{

		/**
		 * The full path to the file being tracked.
		 */
		private final String path;

		/**
		 * Constructs an instance of this class from the supplied parameters.
		 * 
		 * @param path
		 *            The full path to the file to be tracked.
		 * @param marker
		 *            The marker object used to track the file.
		 * @param queue
		 *            The queue on to which the tracker will be pushed.
		 */
		public Tracker(String path, Object marker, ReferenceQueue<Object> queue)
		{
			super(marker, queue);
			this.path = path;
		}

		/**
		 * Deletes the file associated with this tracker instance.
		 * 
		 * @return <code>true</code> if the file was deleted successfully; <code>false</code>
		 *         otherwise.
		 */
		public boolean delete()
		{
		    logger.info("Deleting " + path);
		    FileStorage.instance().delete(path);
			return true;
		}
	}
}

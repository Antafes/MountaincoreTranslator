/*
 * This file is part of Vampire Editor.
 *
 * Vampire Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Vampire Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Vampire Editor. If not, see <http://www.gnu.org/licenses/>.
 *
 * @package Vampire Editor
 * @author Marian Pollzien <map@wafriv.de>
 * @copyright (c) 2019, Marian Pollzien
 * @license https://www.gnu.org/licenses/lgpl.html LGPLv3
 */

package antafes.mountaincoreTranslator.gui;


import antafes.mountaincoreTranslator.Configuration;

import javax.swing.*;
import java.awt.*;
import java.util.function.Function;

class ShowWaitAction
{
    protected static final long SLEEP_TIME = 3 * 1000;
    private Window window;

    public ShowWaitAction(Window window) {
        this.window = window;
    }

    public void show(Function<Void, Void> function) {
        SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>(){
            @Override
            protected Void doInBackground() throws Exception {
                function.apply(null);

                return null;
            }
        };

        final JDialog dialog = new JDialog(this.window, "Dialog", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);

        mySwingWorker.addPropertyChangeListener(evt1 -> {
            if (evt1.getPropertyName().equals("state")) {
                if (evt1.getNewValue() == SwingWorker.StateValue.DONE) {
                    dialog.dispose();
                }
            }
        });
        mySwingWorker.execute();

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(progressBar, BorderLayout.CENTER);
        panel.add(new JLabel("Loading language file..."), BorderLayout.PAGE_START);
        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this.window);
        dialog.setVisible(true);
    }
}

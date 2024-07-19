/*
 * MIT License
 * Copyright 2024 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.config.messaging

import android.os.Handler
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

class ServiceLifecycleDispatcher(provider: LifecycleOwner) {
    private val registry: LifecycleRegistry = LifecycleRegistry(provider)
    private val handler: Handler
    private var lastDispatchRunnable: DispatchRunnable? = null

    init {
        @Suppress("DEPRECATION")
        handler = Handler()
    }

    private fun postDispatchRunnable(event: Lifecycle.Event) {
        lastDispatchRunnable?.run()
        lastDispatchRunnable = DispatchRunnable(registry, event)
        handler.postAtFrontOfQueue(lastDispatchRunnable!!)
    }

    /**
     * Must be a first call in [Service.onCreate] method, even before super.onCreate call.
     */
    fun onServicePreSuperOnCreate() {
        postDispatchRunnable(Lifecycle.Event.ON_CREATE)
    }

    /**
     * Must be a first call in [Service.onBind] method, even before super.onBind
     * call.
     */
    fun onServicePreSuperOnBind() {
        postDispatchRunnable(Lifecycle.Event.ON_START)
    }

    /**
     * Must be a first call in [Service.onStart] or
     * [Service.onStartCommand] methods, even before
     * a corresponding super call.
     */
    fun onServicePreSuperOnStart() {
        postDispatchRunnable(Lifecycle.Event.ON_START)
    }

    /**
     * Must be a first call in [Service.onDestroy] method, even before super.OnDestroy
     * call.
     */
    fun onServicePreSuperOnDestroy() {
        postDispatchRunnable(Lifecycle.Event.ON_STOP)
        postDispatchRunnable(Lifecycle.Event.ON_DESTROY)
    }

    /**
     * [Lifecycle] for the given [LifecycleOwner]
     */
    val lifecycle: Lifecycle
        get() = registry

    internal class DispatchRunnable(
        private val registry: LifecycleRegistry,
        val event: Lifecycle.Event
    ) : Runnable {
        private var wasExecuted = false

        override fun run() {
            if (!wasExecuted) {
                registry.handleLifecycleEvent(event)
                wasExecuted = true
            }
        }
    }
}

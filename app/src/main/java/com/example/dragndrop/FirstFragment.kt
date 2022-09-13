package com.example.dragndrop

import android.content.ClipData
import android.content.ClipDescription
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.dragndrop.databinding.FragmentFirstBinding


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.hello.setOnTouchListener { button, touchEvent ->
            if (touchEvent.action == MotionEvent.ACTION_DOWN) startDragAndDrop(button)
            false
        }
        binding.hola.setOnTouchListener { button, touchEvent ->
            if (touchEvent.action == MotionEvent.ACTION_DOWN) startDragAndDrop(button)
            false
        }
        binding.bonjour.setOnTouchListener { button, touchEvent ->
            if (touchEvent.action == MotionEvent.ACTION_DOWN) startDragAndDrop(button)
            false
        }
        binding.next.setOnClickListener { startActivity(Intent(requireContext(), MultiWindowSourceActivity::class.java)) }
    }

    private fun startDragAndDrop(button: View): Boolean {
        button as Button
        val dragData = getClipData(button)
        button.startDragAndDrop(dragData, View.DragShadowBuilder(button), null, View.DRAG_FLAG_GLOBAL or View.DRAG_FLAG_GLOBAL_URI_READ)
        return true
    }

    private fun getClipData(button: Button) =
        ClipData("data", arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN), ClipData.Item(button.text))

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private class MyDragShadowBuilder(v: View) : View.DragShadowBuilder(v) {
        val shadow = BitmapDrawable(v.resources, loadBitmapFromView(v))
        fun loadBitmapFromView(v: View): Bitmap? {
            v.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            val b = Bitmap.createBitmap(v.measuredWidth, v.measuredHeight, Bitmap.Config.ARGB_8888)
            val c = Canvas(b)
            v.layout(v.left, v.top, v.right, v.bottom)
            v.draw(c)
            return b
        }

        override fun onProvideShadowMetrics(size: Point, touch: Point) {

            // Set the width of the shadow to half the width of the original View.
            val width: Int = view.width

            // Set the height of the shadow to half the height of the original View.
            val height: Int = view.height

            // The drag shadow is a ColorDrawable. This sets its dimensions to be the
            // same as the Canvas that the system provides. As a result, the drag shadow
            // fills the Canvas.
            shadow.setBounds(0, 0, width, height)

            // Set the size parameter's width and height values. These get back to
            // the system through the size parameter.
            size.set(width, height)

            // Set the touch point's position to be in the middle of the drag shadow.
            touch.set(width / 2, height / 2)
        }

        // Defines a callback that draws the drag shadow in a Canvas that the system
        // constructs from the dimensions passed to onProvideShadowMetrics().
        override fun onDrawShadow(canvas: Canvas) {
            shadow.draw(canvas)
        }
    }
}

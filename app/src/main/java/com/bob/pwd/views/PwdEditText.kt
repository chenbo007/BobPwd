package com.bob.pwd.views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.text.InputFilter
import android.util.AttributeSet
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View

import com.bob.pwd.R

/**
 * Created by chenbo on 2017/11/14.
 */

class PwdEditText : android.support.v7.widget.AppCompatEditText {
    private var numPwd = 6
    //临时输入变量
    /**
     * 获取输入的文本
     *
     * @return
     */
    var textStr = ""
    private var ovalSize = 40f
    private var lineColor = Color.GRAY
    private var contentColor = Color.BLACK
    private var paint: Paint? = null
    private var radius = 0f;
    private var onTextChangeListener: OnTextChangeListener? = null
    private var contentType=1;//1:圆点 2：文本
    /**
     * 设置文字变化回调
     */
    fun setOnTextChangeListener(onTextChangeListener: OnTextChangeListener) {
        this.onTextChangeListener = onTextChangeListener
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PwdEditText)
        ovalSize = typedArray.getDimension(R.styleable.PwdEditText_ovalSize, -1f)
        lineColor = typedArray.getColor(R.styleable.PwdEditText_lineColor, Color.GRAY)
        numPwd = typedArray.getInteger(R.styleable.PwdEditText_num, 0)
        contentColor = typedArray.getColor(R.styleable.PwdEditText_contentColor, Color.BLACK)
        contentType=typedArray.getInt(R.styleable.PwdEditText_contentType,1);
        radius = typedArray.getDimension(R.styleable.PwdEditText_radius, 0f);
        typedArray.recycle()
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PwdEditText)
        ovalSize = typedArray.getDimension(R.styleable.PwdEditText_ovalSize, -1f)
        lineColor = typedArray.getColor(R.styleable.PwdEditText_lineColor, Color.GRAY)
        numPwd = typedArray.getInteger(R.styleable.PwdEditText_num, 0)
        contentColor = typedArray.getColor(R.styleable.PwdEditText_contentColor, Color.BLACK)
        contentType=typedArray.getInt(R.styleable.PwdEditText_contentType,1);
        typedArray.recycle()
        init()
    }

    private fun init() {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        setBackgroundColor(Color.TRANSPARENT)
        filters = arrayOf<InputFilter>(InputFilter.LengthFilter(numPwd))
        isCursorVisible = false
        //禁止复制粘贴
        setTextIsSelectable(false)
        setOnLongClickListener(object : OnLongClickListener {
            override fun onLongClick(p0: View?): Boolean {
                return true;
            }
        });
        customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onCreateActionMode(actionMode: ActionMode, menu: Menu): Boolean {
                return false
            }

            override fun onPrepareActionMode(actionMode: ActionMode, menu: Menu): Boolean {
                return false
            }

            override fun onActionItemClicked(actionMode: ActionMode, menuItem: MenuItem): Boolean {
                return false
            }

            override fun onDestroyActionMode(actionMode: ActionMode) {}
        }
    }

    override fun onTextContextMenuItem(id: Int): Boolean {
        return false
    }


    override fun onDraw(canvas: Canvas) {
        paint!!.color = lineColor
        paint!!.style = Paint.Style.STROKE
        canvas.drawRoundRect(RectF(0f, 0f, width.toFloat(), height.toFloat()), radius, radius, paint!!)
        //绘制分割线
        val itemW = width / numPwd
        for (i in 1 until numPwd) {
            val x = (i * itemW).toFloat()
            canvas.drawLine(x, 0f, x, height.toFloat(), paint!!)
        }
        if (contentType==1) {
            paint!!.style = Paint.Style.FILL
            paint!!.color = contentColor
            //绘制圆点
            for (i in 0 until textStr.length) {
                val x = i * itemW + (itemW - ovalSize) / 2
                val y = (height - ovalSize) / 2
                canvas.drawOval(RectF(x, y, ovalSize + x, y + ovalSize), paint!!)
            }
        } else if(contentType==2) {
            //绘制文字
            paint!!.textSize = textSize
            for (i in 0 until textStr.length) {
                val rect = Rect()
                paint!!.getTextBounds(textStr, i, i + 1, rect)
                val x = (i * itemW + (itemW - rect.width()) / 2).toFloat()
                val y = ((height - rect.height()) / 2).toFloat()
                canvas.drawText(textStr, i, i + 1, x, y + rect.height(), paint!!)
            }
        }
    }

    override fun onTextChanged(text: CharSequence, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (text.length <= numPwd) {
            textStr = text.toString()
            if (onTextChangeListener != null) onTextChangeListener!!.onTextChange(textStr)
            invalidate()
        }
    }

    /**
     * 文字变化回调
     */
    interface OnTextChangeListener {
        fun onTextChange(text: String)
    }


}

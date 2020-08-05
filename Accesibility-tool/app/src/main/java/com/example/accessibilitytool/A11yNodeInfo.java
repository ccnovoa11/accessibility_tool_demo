package com.example.accessibilitytool;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build.VERSION;
import androidx.viewpager.widget.ViewPager ;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class A11yNodeInfo implements Iterable<A11yNodeInfo>, Comparator<A11yNodeInfo> {
    private static final ArrayList<Class<? extends View>> ACTIVE_CLASSES = new ArrayList();
    private final AccessibilityNodeInfoCompat mNodeInfo;

    public static String BUTTON = "android.widget.Button";
    public static String IMAGE_BUTTON = "android.widget.ImageButton";
    public static String CHECKBOX = "android.widget.CheckBox";
    public static String IMAGE_VIEW = "android.widget.ImageView";

    public int contentDescriptionIssues = 0;
    public int touchAreaIssues = 0;


    public static A11yNodeInfo wrap(AccessibilityNodeInfo node) {
        return node == null ? null : new A11yNodeInfo(node);
    }

    public static A11yNodeInfo wrap(AccessibilityNodeInfoCompat node) {
        return node == null ? null : new A11yNodeInfo(node);
    }

    protected A11yNodeInfo() {
        this.mNodeInfo = null;
    }

    protected A11yNodeInfo(AccessibilityNodeInfo nodeInfo) {
        this(new AccessibilityNodeInfoCompat(nodeInfo));
    }

    protected A11yNodeInfo(AccessibilityNodeInfoCompat nodeInfoCompat) {
        if (nodeInfoCompat == null) {
            throw new RuntimeException("Wrapping a null node doesn't make sense");
        } else {
            this.mNodeInfo = nodeInfoCompat;
        }
    }

    public int compare(A11yNodeInfo lhs, A11yNodeInfo rhs) {
        int result = lhs.getSpeakableText().compareTo(rhs.getSpeakableText());
        if (result != 0) {
            return result;
        } else {
            Rect lhsRect = lhs.getBoundsInScreen();
            Rect rhsRect = rhs.getBoundsInScreen();
            if (result != 0) {
                return result;
            } else if (lhsRect.top < rhsRect.top) {
                return -1;
            } else if (lhsRect.top > rhsRect.top) {
                return 1;
            } else if (lhsRect.left < rhsRect.left) {
                return -1;
            } else if (lhsRect.left > rhsRect.left) {
                return 1;
            } else if (lhsRect.right < rhsRect.right) {
                return -1;
            } else if (lhsRect.right > rhsRect.right) {
                return 1;
            } else if (lhsRect.bottom < rhsRect.bottom) {
                return -1;
            } else if (lhsRect.bottom > rhsRect.bottom) {
                return 1;
            } else {
                return result != 0 ? result : 0;
            }
        }
    }

    public List<AccessibilityActionCompat> getActionList() {
        return this.mNodeInfo.getActionList();
    }

    public int getActions() {
        return this.mNodeInfo.getActions();
    }

    public boolean isActiveElement() {
        Iterator var1 = ACTIVE_CLASSES.iterator();

        while(var1.hasNext()) {
            Class<? extends View> clazz = (Class)var1.next();
            if (this.getClassName().equalsIgnoreCase(clazz.getName())) {
                return true;
            }
        }

        if (VERSION.SDK_INT >= 21) {
            if (this.getActionList().contains(AccessibilityAction.ACTION_CLICK)) {
                return true;
            }

            if (VERSION.SDK_INT >= 23 && this.getActionList().contains(AccessibilityAction.ACTION_CONTEXT_CLICK)) {
                return true;
            }

            if (this.getActionList().contains(AccessibilityAction.ACTION_LONG_CLICK)) {
                return true;
            }

            if (this.getActionList().contains(AccessibilityAction.ACTION_SELECT)) {
                return true;
            }
        }

        int actions = this.getActions();
        return (actions & 16) != 0 || (actions & 32) != 0 || (actions & 4) != 0;
    }

    public boolean performAction(A11yNodeInfo.Actions action) {
        return this.mNodeInfo.performAction(action.getAndroidValue());
    }

    public AccessibilityNodeInfoCompat getAccessibilityNodeInfoCompat() {
        return this.mNodeInfo;
    }

    public Rect getBoundsInScreen() {
        Rect result = new Rect();
        this.mNodeInfo.getBoundsInScreen(result);
        return result;
    }

    public A11yNodeInfo getChild(int i) {
        if (i >= this.mNodeInfo.getChildCount()) {
            throw new IndexOutOfBoundsException();
        } else {
            return new A11yNodeInfo(this.mNodeInfo.getChild(i));
        }
    }

    public int getChildCount() {
        return this.mNodeInfo.getChildCount();
    }

    public String getClassName() {
        return this.mNodeInfo.getClassName().toString();
    }

    public CharSequence getContentDescription() {
        return this.mNodeInfo.getContentDescription();
    }

    public String getContentDescriptionAsString() {
        return this.mNodeInfo.getContentDescription() == null ? "" : this.mNodeInfo.getContentDescription().toString();
    }

    public int getDepthInTree() {
        int result = 0;

        for(A11yNodeInfo parentNode = this.getParent(); parentNode != null; ++result) {
            parentNode = parentNode.getParent();
        }

        return result;
    }

    public A11yNodeInfo getLabeledBy() {
        return wrap(this.mNodeInfo.getLabeledBy());
    }

    public A11yNodeInfo getParent() {
        return this.mNodeInfo.getParent() == null ? null : new A11yNodeInfo(this.mNodeInfo.getParent());
    }

    public String getSpeakableText() {
        if (this.getContentDescription() != null) {
            return this.getContentDescriptionAsString();
        } else {
            return this.getText() != null ? this.getTextAsString() : "";
        }
    }

    public CharSequence getText() {
        return this.mNodeInfo.getText();
    }

    public String getTextAsString() {
        return this.getText() != null ? this.getText().toString() : "";
    }

    public String getViewIdResourceName() {
        return this.mNodeInfo.getViewIdResourceName() == null ? "" : this.mNodeInfo.getViewIdResourceName();
    }

    public Iterator<A11yNodeInfo> iterator() {
        return new Iterator<A11yNodeInfo>() {
            private int mNextIndex = 0;

            public boolean hasNext() {
                return this.mNextIndex < A11yNodeInfo.this.getChildCount() && (A11yNodeInfo.this.mNodeInfo == null || A11yNodeInfo.this.mNodeInfo.getChild(this.mNextIndex) != null);
            }

            public A11yNodeInfo next() {
                return A11yNodeInfo.this.getChild(this.mNextIndex++);
            }

            public void remove() {
            }
        };
    }

    public String toViewHeirarchy() {
        final StringBuilder result = new StringBuilder();
        result.append("<h3> Accessibility Event Detected </h3>\n");
        this.visitNodes(new A11yNodeInfo.OnVisitListener() {
            public boolean onVisit(A11yNodeInfo nodeInfo) {
                elementHasIssue(nodeInfo);
                result.append("Element: "+nodeInfo.getClassName() + "<br>");
                result.append("Text: "+nodeInfo.getTextAsString() + "<br>");
                result.append("Content Description: "+nodeInfo.getContentDescription() + "<br>");
                result.append("Width: "+ convertPixelsToDp(nodeInfo.getBoundsInScreen().width()) + "<br>");
                result.append(" Height: "+convertPixelsToDp(nodeInfo.getBoundsInScreen().height()) + "<br>");
                result.append("-------------------------------------------------------------------<br>");

//                result.append('\n');
                return false;
            }

        });
        result.append("Elements with contentDescription issues: " + contentDescriptionIssues + "<br>");
        result.append("Elements with touchArea issues: " + touchAreaIssues);
        resetValues();
        return result.toString();
    }

    public void elementHasIssue(A11yNodeInfo nodeInfo){
        if (nodeInfo.getClassName().equals(BUTTON) || nodeInfo.getClassName().equals(IMAGE_BUTTON) || nodeInfo.getClassName().equals(CHECKBOX)){
            if (nodeInfo.getTextAsString().equals(null) || nodeInfo.getContentDescription().toString().equals(null)){
                contentDescriptionIssues++;
            }
            if (convertPixelsToDp(nodeInfo.getBoundsInScreen().width())<48 && convertPixelsToDp(nodeInfo.getBoundsInScreen().height())<48){
                touchAreaIssues++;
            }
        }
    }

    public void resetValues(){
        contentDescriptionIssues = 0;
        touchAreaIssues = 0;
    }

    public static float convertPixelsToDp(float px){
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }


    public boolean isClassType(Class<?> clazz) {
        return clazz.getName().equalsIgnoreCase(this.getClassName());
    }

    public boolean isScrollable() {
        return this.mNodeInfo.isScrollable();
    }

    public boolean isVisibleToUser() {
        return this.mNodeInfo.isVisibleToUser();
    }

    public boolean isInVisibleScrollableField() {
        A11yNodeInfo tempNode = wrap(this.mNodeInfo);

        A11yNodeInfo scrollableView;
        for(scrollableView = null; tempNode.getParent() != null; tempNode = tempNode.getParent()) {
            if (tempNode.isScrollable() && !tempNode.isClassType(ViewPager.class)) {
                scrollableView = tempNode;
            }
        }

        return scrollableView != null && scrollableView.isVisibleToUser();
    }

    public String toString() {
        if (this.mNodeInfo == null) {
            throw new RuntimeException("This shouldn't be null");
        } else {
            return this.mNodeInfo.toString();
        }
    }

    public A11yNodeInfo visitNodes(A11yNodeInfo.OnVisitListener onVisitListener) {
        if (onVisitListener.onVisit(this)) {
            return this;
        } else {
            Iterator var2 = this.iterator();

            A11yNodeInfo result;
            do {
                if (!var2.hasNext()) {
                    return null;
                }

                A11yNodeInfo child = (A11yNodeInfo)var2.next();
                result = child.visitNodes(onVisitListener);
            } while(result == null);

            return result;
        }
    }

    static {
        ACTIVE_CLASSES.add(Button.class);
        ACTIVE_CLASSES.add(Switch.class);
        ACTIVE_CLASSES.add(CheckBox.class);
        ACTIVE_CLASSES.add(EditText.class);
    }

    public interface OnVisitListener {
        boolean onVisit(A11yNodeInfo var1);
    }

    public static enum Actions {
        ACCESSIBILITY_FOCUS(64),
        CLEAR_ACCESSIBILITY_FOCUS(128),
        CLEAR_FOCUS(2),
        CLEAR_SELECTION(8),
        CLICK(16),
        COLLAPSE(524288),
        COPY(16384),
        CUT(65536),
        LONG_CLICK(32),
        PASTE(32768),
        PREVIOUS_AT_MOVEMENT_GRANULARITY(512),
        PREVIOUS_HTML_ELEMENT(2048);

        private final int mAndroidValue;

        private Actions(int androidValue) {
            this.mAndroidValue = androidValue;
        }

        int getAndroidValue() {
            return this.mAndroidValue;
        }
    }
}
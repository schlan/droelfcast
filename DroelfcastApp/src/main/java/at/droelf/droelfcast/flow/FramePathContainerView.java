package at.droelf.droelfcast.flow;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import at.droelf.droelfcast.R;
import flow.path.Path;
import flow.path.PathContainer;
import flow.path.PathContainerView;
import flow.Flow;
import flow.path.PathContextFactory;


/** A FrameLayout that can show screens for a {@link flow.Flow}. */
public class FramePathContainerView extends FrameLayout
        implements HandlesBack, PathContainerView {
  private final PathContainer container;
  private boolean disabled;

  @SuppressWarnings("UnusedDeclaration") // Used by layout inflation, of course!
  public FramePathContainerView(Context context, AttributeSet attrs) {
    this(context, attrs, new SimplePathContainer(R.id.screen_switcher_tag, Path.contextFactory(new PathContextFactory() {
      @Override
      public Context setUpContext(Path path, Context parentContext) {
        return null;
      }

      @Override
      public void tearDownContext(Context context) {

      }
    })));
  }

  /**
   * Allows subclasses to use custom {@link flow.path.PathContainer} implementations. Allows the use
   * of more sophisticated transition schemes, and customized context wrappers.
   */
  protected FramePathContainerView(Context context, AttributeSet attrs, PathContainer container) {
    super(context, attrs);
    this.container = container;
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    return !disabled && super.dispatchTouchEvent(ev);
  }

  @Override
  public ViewGroup getContainerView() {
    return this;
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
  }

  @Override
  public void dispatch(Flow.Traversal traversal, final Flow.TraversalCallback callback) {
    disabled = true;
    container.executeTraversal(this, traversal, new Flow.TraversalCallback() {
      @Override
      public void onTraversalCompleted() {
        callback.onTraversalCompleted();
        disabled = false;
      }
    });
  }

  @Override
  public boolean onBackPressed() {
    return BackSupport.onBackPressed(getCurrentChild());
  }

  @Override
  public ViewGroup getCurrentChild() {
    return (ViewGroup) getContainerView().getChildAt(0);
  }
}
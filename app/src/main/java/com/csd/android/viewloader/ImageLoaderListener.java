package com.csd.android.viewloader;

import android.net.Uri;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.csd.android.R;
import com.csd.android.activity.TaskDetailActivity;

public class ImageLoaderListener implements RequestListener<Object, GlideDrawable> {

	private TaskDetailActivity context;
	private Uri uri;

	public ImageLoaderListener(TaskDetailActivity context, Uri url) {
		this.context = context;
		this.uri = url;
	}

	@Override
	public boolean onException(Exception e, Object model, Target<GlideDrawable> target, boolean isFirstResource) {
		((GlideDrawableImageViewTarget) target).getView().setTag(R.id.tag_glide, "fail");
		((GlideDrawableImageViewTarget) target).getView().setOnClickListener(new PhotoViewClickListener(context, (Uri) model));
		((GlideDrawableImageViewTarget) target).getView().setOnLongClickListener(new PhotoViewLongClickListener(context));
		return false;
	}

	@Override
	public boolean onResourceReady(GlideDrawable resource, Object model, Target<GlideDrawable> target, boolean isFromMemoryCache,
			boolean isFirstResource) {
		((GlideDrawableImageViewTarget) target).getView().setTag(R.id.tag_glide, "success");
		((GlideDrawableImageViewTarget) target).getView().setOnClickListener(new PhotoViewClickListener(context, uri));
		return false;
	}
}

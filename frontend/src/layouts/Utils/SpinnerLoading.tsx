export const SpinnerLoading = () => {
    return (
        /*
        container: This is likely a Bootstrap class (a CSS framework) that makes the element take up a certain portion of the screen with a maximum width.

        d-flex: A Bootstrap class that makes the div behave as a flex container, allowing it to align and arrange child elements (like the spinner).
         */
        <div className='container m-5 d-flex justify-content-center' 
            style={{ height: 550 }}>
                {/*
                    role='status': The role attribute is used for accessibility. It tells screen readers that the div is important and represents a status (in this case, it's indicating a loading state).
                */}
                <div className='spinner-border text-primary' role='status'>
                    {/*
                        className='visually-hidden': This is a Bootstrap class used for accessibility. It hides the text visually but still allows screen readers to read it, so users with disabilities can understand what's happening (in this case, "Loading...").

                         "Loading...": This is the text shown for assistive technologies like screen readers, but the text won't be visible on the screen because of the visually-hidden class.
                    */}
                    <span className='visually-hidden'>
                        Loading...
                    </span>
                </div>
        </div>
    );
}
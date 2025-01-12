import React from 'react';
import { Navigate, useNavigate } from 'react-router-dom';
import './App.css';
import { BookCheckoutPage } from './layouts/BookCheckoutPage/BookCheckoutPage';
import { HomePage } from './layouts/HomePage/HomePage';
import { Footer } from './layouts/NavbarAndFooter/Footer';
import { Navbar } from './layouts/NavbarAndFooter/Navbar';
import { SearchBooksPage } from './layouts/SearchBooksPage/SearchBooksPage';
import { oktaConfig } from './lib/oktaConfig';
import { OktaAuth, toRelativeUrl } from '@okta/okta-auth-js';
import { Security, LoginCallback, SecureRoute } from '@okta/okta-react';
import LoginWidget from './Auth/LoginWidget';
import { ReviewListPage } from './layouts/BookCheckoutPage/ReviewListPage/ReviewListPage';
import { ShelfPage } from './layouts/ShelfPage/ShelfPage';
import { MessagesPage } from './layouts/MessagesPage/MessagesPage';
import { ManageLibraryPage } from './layouts/ManageLibraryPage/ManageLibraryPage';
import { Route, Routes } from 'react-router-dom';

const oktaAuth = new OktaAuth(oktaConfig);

export const App = () => {
  const history = useNavigate();

  const customAuthHandler = () => {
    history('/login');
  }

  const restoreOriginalUri = async (_oktaAuth: any, originalUri: any) => {
    history(toRelativeUrl(originalUri || '/', window.location.origin), { replace: true });
  };


  return (
    <div className='d-flex flex-column min-vh-100'>
      <Security oktaAuth={oktaAuth} restoreOriginalUri={restoreOriginalUri} onAuthRequired={customAuthHandler}>

        {/* HEADER SECTION */}
        <Navbar />

        {/* CONTENT */}
        <div className='flex-grow-1'>
        <Routes>
          <Route path='/' element={<HomePage />} />
          <Route path='/home' element={<HomePage />} />
          <Route path='/search' element={<SearchBooksPage />} />
          <Route path='/reviewlist/:bookId' element={<ReviewListPage />} />
          <Route path='/checkout/:bookId' element={<BookCheckoutPage />} />
          <Route path='/login' element={<LoginWidget config={oktaConfig} />} />
          <Route path='/login/callback' element={<LoginCallback />} />

          <Route path='/shelf' element={<ShelfPage />} />
          <Route path='/messages' element={<MessagesPage />} />
          <Route path='/admin' element={<ManageLibraryPage />} />
      </Routes>

        </div>


        {/* FOOTER SECTION */}
        <Footer />
      
      </Security>

    </div>
  );
}

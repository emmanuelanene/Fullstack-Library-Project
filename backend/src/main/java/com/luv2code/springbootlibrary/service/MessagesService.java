package com.luv2code.springbootlibrary.service;

import com.luv2code.springbootlibrary.dao.MessageRepository;
import com.luv2code.springbootlibrary.entity.Message;
import com.luv2code.springbootlibrary.requestmodels.AdminQuestionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class MessagesService {

    private MessageRepository messageRepository;

    @Autowired
    public MessagesService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /*
        Whenever a user wants to post a new message, the hero jumps into action. The user sends their message (contained in messageRequest) along with their email.

        The service grabs the title and question from the user's message.
        A new Message object is created using that title and question.
        Then, the service sets the user's email (since the user who is sending the message needs to be linked to it).
        Finally, the sidekick MessageRepository is called to save this message into the database.
     */
    public void postMessage(Message messageRequest, String userEmail) {
        Message message = new Message(messageRequest.getTitle(), messageRequest.getQuestion());
        message.setUserEmail(userEmail);
        messageRepository.save(message);
    }


    /*
    Later on, when an admin wants to respond to a user’s question, the hero has a second task: updating the message with the admin’s reply.

        First, the service searches for the existing message in the database using the message’s ID (from the AdminQuestionRequest).
        If the message isn’t found (it doesn’t exist in the database), the service throws an error: "Message not found".
        But if the message is found, the hero then:
        Sets the admin’s email on the message (to show who responded).
        Adds the admin’s response (contained in adminQuestionRequest).
        Marks the message as closed (since the admin has replied and the conversation is complete).
        After all that, the sidekick, MessageRepository, is called again to save the updated message.
     */
    public void putMessage(AdminQuestionRequest adminQuestionRequest, String userEmail) throws Exception {
        // We are using this to confirm if the ID that was inputed for the Admin to respond to is actually a valid ID
        Optional<Message> message = messageRepository.findById(adminQuestionRequest.getId());

        // If no, then the message doesn't exists, hence the Admin cannot reply to it
        if (!message.isPresent()) {
            throw new Exception("Message not found");
        }

        message.get().setAdminEmail(userEmail);
        message.get().setResponse(adminQuestionRequest.getResponse());
        message.get().setClosed(true);
        messageRepository.save(message.get());
    }

}

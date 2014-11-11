require 'spec_helper'

def login_user_bill
  visit HOST
  fill_in('Username', :with => 'bill')
  fill_in('Password', :with => 'admin')
  click_button('Sign in')
end

def login_user_ben
  visit HOST
  fill_in('Username', :with => 'ben')
  fill_in('Password', :with => 'admin')
  click_button('Sign in')
end

def login_user_jeff
  visit HOST
  fill_in('Username', :with => 'jeff')
  fill_in('Password', :with => 'admin')
  click_button('Sign in')
end

def logout
  find('.t-user-settings').click
  click_link('Sign out')
end




def go_to_timeline
  find('.t-nav-timeline').click
end
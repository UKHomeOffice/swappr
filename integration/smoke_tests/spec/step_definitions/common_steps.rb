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

def logout
  find('.t-user-settings').click
  click_link('Sign out')
end

def add_shift(day_index, shift_type)
  days = page.all('.t-day')
  days[day_index].find('.t-add-shift').click
  select(shift_type, :from => 'Shift')
  click_on('Add')
end

def offer_shift(day_index, shift_type_from, shift_type_to)
  add_shift(day_index, shift_type_from)
  days = page.all('.t-day')
  days[day_index].find('.t-swap-shift').click
  select(shift_type_to, :from => 'toShiftType')
  click_on('Request swap')
end

def find_date_by_row(row_index)
  page.all('.t-day')[row_index]['data-t-date']
end

def find_day_row_by_date(date)
  find(:xpath, "//*[@data-t-date='" + date + "']")
end

def assert_status_for_date(date, status)
  expect(find_day_row_by_date(date).find('.t-status')).to have_content(status)
end

def assert_shift_details_for_date(date, text)
  expect(find_day_row_by_date(date).find('.t-shift')).to have_content(text)
end

def assert_last_event_heading(heading)
  expect(page.all('.t-event-heading')[0]).to have_content(heading)
end

def go_to_swap_view_for_date(date)
  find_day_row_by_date(date).find('.t-link-swap-view').click
end

def go_to_timeline
  find('.t-nav-timeline').click
end
require 'spec_helper'
require 'step_definitions/common_steps'
require 'step_definitions/home_page_common_steps'
require 'step_definitions/swap_view_steps'

feature "Full journey - offer shift, accept volunteer, mark as approved" do

  background do
    login_user_bill
  end

  scenario "You can offer to swap a shift" do
    date = find_date_by_row(3)
    offer_shift(3, 'BFH - Early', 'S1H - Late')
    assert_status_for_date(date, 'Swap requested')
    logout
    login_user_ben
    go_to_timeline
    assert_shift_details_for_date(date, "Bill Beetroot wants to swap")
  end

  scenario "You can volunteer to swap for an offered shift" do
    date = find_date_by_row(5)
    offer_shift(5, 'CFH - Mid', 'C1H - Mid')
    logout
    login_user_ben
    go_to_timeline
    find_day_row_by_date(date).find('.t-button-volunteer').click
    assert_status_for_date(date, "You have replied")
    logout
    login_user_bill
    assert_status_for_date(date, "Ben Bernanke has replied")
    go_to_swap_view_for_date(date)
    assert_last_event_heading("Ben Bernanke replied")
    click_on('Accept offer')
    assert_first_event_heading("Next steps")
    click_on('Approved')
    assert_status_for_date(date, "Approved. Swapped with Ben Bernanke")
    

  end

  scenario "You are able to view the details of your offer throughout its lifecycle" do
    date = find_date_by_row(7)
    offer_shift(7, 'CFH - Mid', 'C1H - Mid')
    logout
    login_user_ben
    go_to_timeline
    find_day_row_by_date(date).find('.t-button-volunteer').click
    logout

    login_user_jeff
    go_to_timeline
    find_day_row_by_date(date).find('.t-button-volunteer').click
    logout

    login_user_bill

    go_to_swap_view_for_date(date)
    assert_first_event_heading("Jeff Jangles replied")
    assert_last_event_heading("Ben Bernanke replied")
    all('.submit-button', :text => 'Accept offer')[0].click()

    assert_your_new_shift_details('You', 'C1H', date)
    assert_their_new_shift_details('Jeff Jangles', 'CFH', date)
  end

end